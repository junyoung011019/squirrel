import json
from db import get_connection # '공용 부품' 가져오기

def lambda_handler(event, context):
    connection = None
    cursor = None
    try:
        # '공용 부품'을 사용해 커넥션을 간단히 빌려옴
        connection = get_connection()
        # 결과를 dict 형태로 받기 위해 dictionary=True 옵션 추가
        cursor = connection.cursor(dictionary=True)

        # --- DB 작업 수행 ---
        query = "SELECT * FROM user"
        cursor.execute(query)

        # 2. 모든 결과를 리스트로 가져옴
        user_records = cursor.fetchall()

        # 3. 조회된 모든 기록을 JSON 형태로 반환
        return {
            "statusCode": 200,
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps(user_records, default=str) # datetime 같은 타입을 문자열로 변환
        }

    except Exception as e:
        print(f"An error occurred: {e}")
        return { "statusCode": 500, "body": json.dumps({"error": str(e)}) }
    finally:
        # 사용이 끝나면 커넥션을 풀에 '반납'
        if connection and connection.is_connected():
            if cursor:
                cursor.close()
            connection.close()