import json
import os
from db import get_connection

def lambda_handler(event, context):

    db_host_value = os.environ.get("DB_HOST", "NOT_FOUND")

    headers = event.get("headers", {}) or {}
    lower_case_headers = {k.lower(): v for k, v in headers.items()}
    user_uuid = lower_case_headers.get("uuid")

    if not user_uuid:
        # 헤더에 UUID가 없을 경우 거부
        return generate_policy("anonymous", "Deny", event["methodArn"], reason="Missing user_uuid header")

    connection = None
    cursor = None

    try:
        connection = get_connection()
        cursor = connection.cursor(dictionary=True)

        # DB에서 uuid로 gender 조회
        cursor.execute("SELECT gender FROM user WHERE uuid = %s", (user_uuid,))
        user = cursor.fetchone()

        if not user:
            # uuid 존재하지 않음 → 접근 거부
            return generate_policy(
                principal_id=user_uuid,
                effect="Deny",
                resource=event["methodArn"],
                reason="Invalid user_uuid"
            )

        gender = user.get("gender")

        # 성공 → uuid와 gender를 context에 담아 다음 Lambda로 전달
        return generate_policy(
            principal_id=user_uuid,
            effect="Allow",
            resource=event["methodArn"],
            reason="Authorized",
            extra_context={"user_uuid": user_uuid, "gender": gender}
        )

    except Exception as e:
        print(f"[Authorizer] Error: {e}")
        return generate_policy(
            principal_id="anonymous",
            effect="Deny",
            resource=event["methodArn"],
            reason=f"Internal error: {str(e)}"
        )

    finally:
        if connection and connection.is_connected():
            if cursor:
                cursor.close()
            connection.close()


def generate_policy(principal_id, effect, resource, reason="", extra_context=None):
    context = {"reason": reason}
    if extra_context:
        context.update(extra_context)

    policy = {
        "principalId": principal_id,
        "policyDocument": {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Action": "execute-api:Invoke",
                    "Effect": effect,
                    "Resource": resource
                }
            ]
        },
        "context": context
    }
    return policy
