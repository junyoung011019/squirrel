import json
import boto3
import os
from boto3.dynamodb.conditions import Key
from decimal import Decimal

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table(os.environ['TABLE_NAME'])

def decimal_serializer(obj):
    if isinstance(obj, Decimal):
        # 숫자가 정수이면 int, 아니면 float으로 변환
        if obj % 1 == 0:
            return int(obj)
        else:
            return float(obj)
    # 처리할 수 없는 타입은 TypeError를 발생시켜야 합니다.
    raise TypeError(f"Object of type {type(obj).__name__} is not JSON serializable")

def lambda_handler(event, context):
    try:
        response = table.scan()
        items = response.get('Items', [])

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json",
                "Access-Control-Allow-Origin": "*"
            },
            "body": json.dumps(items, default=decimal_serializer, ensure_ascii=False)
        }
    except Exception as e:
        print(f"오류 발생: {e}")
        return {
            "statusCode": 500,
            "body": json.dumps({"message": "오류가 발생했습니다."})
        }