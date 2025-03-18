# TreeCore Library

TreeCore는 계층적 트리 구조를 효율적으로 관리하기 위한 Java 라이브러리입니다.  

[Nested Set Model에 대한 자세한 내용 보기](https://float.tistory.com/339#google_vignette)  

Nested Set Model을 기반으로 구현되어 있어 대규모 트리 구조에서도 빠른 조회와 효율적인 수정이 가능합니다.


## 🌟 주요 특징

### 1. Nested Set Model 기반 구현
- **빠른 조회 성능**: O(1) 시간에 자식 노드 조회 가능
- **효율적인 트리 순회**: 단일 쿼리로 모든 자식/조상 노드 조회
- **계층 구조 유지**: 트리 구조의 무결성 보장

### 2. 강력한 트리 조작 기능
- **노드 추가/삭제**: 트리 구조를 유지하면서 노드 추가/삭제
- **노드 이동**: 트리 내에서 노드 위치 변경
- **노드 타입 변경**: 폴더/문서 등 다양한 노드 타입 지원

### 3. 안전한 동시성 처리
- **트랜잭션 격리**: SERIALIZABLE 격리 수준으로 데이터 일관성 보장
- **락킹 메커니즘**: 동시 수정 시 데이터 정합성 유지

### 4. 유연한 확장성
- **커스텀 노드 타입**: 다양한 용도에 맞는 노드 타입 정의 가능
- **추가 속성 지원**: 설명, 상태 등 추가 정보 저장 가능

## 🚀 시작하기

### 의존성 추가
```gradle
dependencies {
    implementation 'com.treecore:treecore:1.0.0'
}
```

### 기본 사용 예시
```java
@Autowired
private TreeNodeService treeNodeService;

// 루트 노드 생성
TreeNodeDTO root = treeNodeService.createRootNode("프로젝트", "folder");

// 자식 노드 추가
TreeNodeDTO child = treeNodeService.addNode(new TreeNodeDTO()
    .setParentId(root.getId())
    .setTitle("문서")
    .setType("document"));

// 자식 노드 조회
List<TreeNodeDTO> children = treeNodeService.getChildNodes(root.getId());
```

## 💡 기술적 장점

### 1. 성능 최적화
- **인덱스 활용**: left, right 값에 대한 인덱스로 빠른 조회
- **배치 처리**: 대량의 노드 수정 시 효율적인 처리
- **캐시 친화적**: 트리 구조가 메모리 캐시에 효율적으로 저장

### 2. 데이터 정합성
- **트리 구조 보장**: Nested Set Model로 트리 구조의 무결성 유지
- **동시성 제어**: 트랜잭션과 락킹으로 데이터 일관성 보장
- **자동 위치 조정**: 노드 추가/삭제 시 자동으로 위치 값 조정

### 3. 확장성
- **모듈화**: 독립적인 라이브러리로 다양한 프로젝트에 통합 가능
- **커스터마이징**: 요구사항에 맞는 노드 타입과 속성 추가 가능
- **API 유연성**: RESTful API와 다양한 조회/수정 메서드 제공

## 🔒 보안

- **코드 난독화**: ProGuard를 통한 코드 보호
- **라이선스 검증**: 라이선스 체크 메커니즘으로 무단 사용 방지
- **접근 제어**: API 레벨의 접근 제어 지원

## 📚 API 문서

### 주요 메서드
- `createRootNode(String title, String type)`: 루트 노드 생성
- `addNode(TreeNodeDTO node)`: 새 노드 추가
- `getChildNodes(Long parentId)`: 자식 노드 조회
- `moveNode(Long nodeId, Long newParentId, Long newPosition)`: 노드 이동
- `removeNode(Long nodeId)`: 노드 삭제
- `updateNode(TreeNodeDTO node)`: 노드 정보 수정

## 🛠 기술 스택

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database (기본)
- Lombok

## 📄 라이선스

이 라이브러리는 독점 라이선스로 보호됩니다. 무단 사용, 수정, 배포가 금지됩니다.

## 🤝 기여하기
이 프로젝트는 현재 독점 라이브러리로 관리되고 있습니다. 
기술적 제안이나 버그 리포트는 이슈 트래커를 통해 제출해 주세요.