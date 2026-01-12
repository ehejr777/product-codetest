## 리뷰는 해당파일 소스에 주석으로 적어두었습니다.

## AI 사용 내역

- 사용 목적:
    - 레거시 코드 리뷰 관점 정리
    - REST API 설계 / 예외 처리 / 트랜잭션 경계 체크리스트 정리

- 사용 도구:
    - ChatGPT (코드 리뷰 및 개선 포인트 브레인스토밍 용도)

- AI 제안 중 채택한 내용:
    1. Entity 직접 반환 → Response DTO 분리
        - 이유: API Contract 안정성, Lazy Loading 이슈 방지
    2. RuntimeException → Custom Exception + ControllerAdvice
        - 이유: HTTP 상태 코드 명확화, 에러 응답 일관성
    3. Service 계층에 @Transactional 명시
        - 이유: Dirty Checking 및 롤백 보장
- 최종 판단:
    - 모든 변경 사항은 실제 운영/협업 환경에서 발생 가능한
      장애 및 유지보수 비용을 기준으로 선별 적용함