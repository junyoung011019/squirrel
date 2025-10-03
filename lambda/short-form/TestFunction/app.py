import os
import json
import mysql.connector
from mysql.connector import Error

# DB 접속 정보는 Lambda 환경 변수에서 가져옵니다.
DB_HOST = os.environ.get('DB_HOST')
DB_USER = os.environ.get('DB_USER')
DB_PASSWORD = os.environ.get('DB_PASSWORD')
DB_NAME = os.environ.get('DB_NAME')

def lambda_handler(event, context):
    """
    users 테이블의 모든 정보를 RDS MySQL에서 조회합니다.
    """
    connection = None
    cursor = None
    try:
        # 1. 데이터베이스 연결
        connection = mysql.connector.connect(
            host=DB_HOST,
            user=DB_USER,
            password=DB_PASSWORD,
            database=DB_NAME
        )

        if connection.is_connected():
            cursor = connection.cursor(dictionary=True) # 결과를 dict 형태로 받기
            
            # 2. SQL 쿼리 수정 (모든 사용자 조회)
            query = "SELECT * FROM user"
            cursor.execute(query) # 파라미터 없이 실행
            
            # 3. 모든 결과 가져오기
            user_records = cursor.fetchall()

            # 4. 조회된 모든 사용자 기록을 반환
            return {
                "statusCode": 200,
                "headers": {"Content-Type": "application/json"},
                "body": json.dumps(user_records, default=str) # datetime 객체 변환을 위해
            }

    except Error as e:
        print(f"Database error: {e}")
        return {"statusCode": 500, "body": json.dumps({"error": "Database connection failed"})}
    except Exception as e:
        print(f"An error occurred: {e}")
        return {"statusCode": 500, "body": json.dumps({"error": "An internal error occurred"})}
    finally:
        # 5. 연결 종료
        if connection and connection.is_connected():
            if cursor:
                cursor.close()
            connection.close()