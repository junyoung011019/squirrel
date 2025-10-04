import json
import uuid
from db import get_connection
import datetime

def lambda_handler(event, context):
    connection = None
    cursor = None
    try:
        request_body = json.loads(event.get("body", "{}"))
        nickname = request_body.get("nickname")
        gender = request_body.get("gender")
        
        # 닉네임이 없는 경우 에러 처리
        if not nickname:
            return {
                "statusCode": 400, # Bad Request
                "body": json.dumps({"error": "Nickname is required."})
            }
        
        # 성별이 없는 경우 에러 처리
        if not gender or (gender not in ('M', 'F')):
            return {
                "statusCode": 400, # Bad Request
                "body": json.dumps({"error": "gender is required."})
            }
        
        now = datetime.datetime.now()

        connection = get_connection()
        cursor = connection.cursor()

        # --- DB 작업 수행 ---
        new_uuid = str(uuid.uuid4())

        query = "INSERT INTO user (nickname, uuid, gender, created_at) VALUES (%s, %s, %s, %s)"
        
        cursor.execute(query, (nickname, new_uuid, gender, now))
        connection.commit()

        return {
            "statusCode": 201,
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "User created successfully.",
                "uuid": new_uuid,
                "nickname": nickname
            })
        }

    except Exception as e:
        if connection:
            connection.rollback()
        print(f"An error occurred: {e}")
        return {"statusCode": 500, "body": json.dumps({"error": str(e)})}
    finally:
        # 사용이 끝나면 커넥션을 풀에 '반납'
        if connection and connection.is_connected():
            if cursor:
                cursor.close()
            connection.close()