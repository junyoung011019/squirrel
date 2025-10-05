# common/python/db.py

import os
import mysql.connector
from mysql.connector import pooling

# Lambda 실행 환경이 살아있는 동안 재사용될 수 있도록 전역 변수로 선언
db_pool = None

def get_connection():
    """
    미리 생성된 커넥션 풀에서 DB 커넥션을 가져옵니다.
    풀이 없으면 새로 생성합니다.
    """
    global db_pool
    if db_pool is None:
        print("DB 커넥션 풀을 새로 생성합니다...")
        try:
            pool_config = {
                "host": os.environ.get('DB_HOST'),
                "user": os.environ.get('DB_USER'),
                "password": os.environ.get('DB_PASSWORD'),
                "database": os.environ.get('DB_NAME'),
                "pool_name": "lambda_pool",
                "pool_size": 5, # 풀에 유지할 커넥션 개수
            }
            db_pool = pooling.MySQLConnectionPool(**pool_config)
            print("DB 커넥션 풀 생성 완료.")
        except Exception as e:
            print(f"DB 커넥션 풀 생성 중 오류 발생: {e}")
            raise e
    
    # print("풀에서 커넥션을 빌려옵니다.")
    return db_pool.get_connection()