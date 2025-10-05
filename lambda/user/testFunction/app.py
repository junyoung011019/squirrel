import json

def lambda_handler(event, context):
    """
    Authorizer를 통과한 요청에 대해 간단한 환영 메시지와
    Authorizer가 전달해준 사용자 정보를 응답으로 보냅니다.
    """
    
    print("Event received by test function:", json.dumps(event))
    
    # Authorizer가 검증 후 context에 담아 전달해준 사용자 정보
    # 이 부분이 Authorizer와 함수가 잘 연동되었는지 확인하는 핵심입니다.
    authorizer_context = event.get("requestContext", {}).get("authorizer", {})
    user_uuid = authorizer_context.get("user_uuid")
    gender = authorizer_context.get("gender")

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*"
        },
        "body": json.dumps({
            "message": "Hello from Test Function!",
            "authorized_user_uuid": user_uuid,
            "authorized_user_gender": gender
        })
    }