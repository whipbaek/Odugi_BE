# Odugi_BE

### 프로젝트 목적

[Smilegate DevCamp 에서 진행한 프로젝트](https://github.com/sgdevcamp2022/ottogi) 를 유지보수 하기 위해 시작한 프로젝트.


### 프로젝트 구성

- Discord 와 유사한 기능을 가진 메신저 프로젝트
- Backend 만 담당하며, Java/Spring Framework로 개발
- Micro Service Architecture 로 구성

![image](https://user-images.githubusercontent.com/75191916/270283124-59d02c87-e174-412e-baa3-73ff5a69cd04.png)


### 프로젝트 목표

- 코드 리팩토링
- 코드 분석력 높이기 (내가 작성한 코드지만 낯설다..)
- API 문서화 및 정리
- 부족한 기능 추가 개발
- 단, 자가학습을 위한 프로젝트이므로 배포는 우선 고려안함 (RDS나 S3와 같은 데이터 저장소는 사용예정)

### 진행 Flow

- Discovery Server -> Gateway -> 각 서비스 별로 훑어보기.
- 그 때마다 모르는 개념이 있으면 학습하고, 리팩토링 할것이 눈에 띄면 수정한다.
- 해야할 학습 목록이나 리팩토링 할 코드는 체크포인트로 설정하여 수행하도록 함.