import os
import ffmpeg

# M4A 파일이 있는 폴더 경로 (사용자 환경에 맞게 수정)
input_folder = "demo 1/m4a"

# 변환된 MP3 파일을 저장할 폴더 경로
output_folder = "demo 1/mp3"

# 입력 폴더 내의 모든 파일 목록 가져오기
try:
    file_list = os.listdir(input_folder)
except FileNotFoundError:
    print(f"오류: 입력 폴더 '{input_folder}'를 찾을 수 없습니다. 경로를 확인해주세요.")
    exit()

print(f"총 {len(file_list)}개의 파일 변환을 시작합니다.")

# 각 파일을 순회하며 변환 작업 수행
for filename in file_list:
    # 파일 확장자가 .m4a인 경우에만 처리
    if filename.lower().endswith(".m4a"):
        input_file_path = os.path.join(input_folder, filename)
        # 출력 파일 이름 설정 (확장자만 .mp3로 변경)
        output_filename = os.path.splitext(filename)[0] + ".mp3"
        output_file_path = os.path.join(output_folder, output_filename)

        print(f"'{filename}' 변환 중...")

        try:
            # 입력 파일을 지정하고 출력 파일 경로와 오디오 비트레이트를 설정
            ffmpeg.input(input_file_path).output(
                output_file_path,
                audio_bitrate='192k'  # 오디오 품질 설정 (e.g., '192k', '320k')
            ).run(overwrite_output=True, quiet=True) # quiet=True로 ffmpeg 로그 숨김

            print(f"-> '{output_filename}' 저장 완료!")

        except ffmpeg.Error as e:
            print(f"'{filename}' 변환 중 오류 발생:", e.stderr.decode())

print("\n모든 작업이 완료되었습니다.")