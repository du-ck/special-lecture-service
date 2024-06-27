# 특강 신청 서비스
## ❗ **Api SPEC**
- GET `/lectures/` : 신청 가능한 특강 목록을 조회한다. 날짜가 지난 수업이 아니라면, 수업시간 전까지는 조회된다.
- POST `/lectures/apply` : 특강을 신청한다.
  <br>userId 기준으로 선착순으로하며 같은 날짜, 같은 강사, 같은 특강타입의 수업은 중복으로 신청할 수 없다.
- GET `/lectures/application/{userId}` : 신청완료 여부를 조회한다.

## 📄 **ERD**
![ERD](https://github.com/du-ck/special-lecture-service/assets/45410646/dfe21c7e-58c8-4e26-b0a1-5dcd3d17ff31)

## 🎲 **Etc...**
- TDD top-down 형식으로 개발
- DB lock(비관락) 적용
- StampedLock 적용
- 레이어드 아키텍쳐 및 DIP 적용
