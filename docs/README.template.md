# UML

```
./gradlew docs:render
```

## Architecture
簡易的なOnion Architectureを採用する。

Stateは、アプリケーションの状態管理を担うため、他のドメインから横断的に利用される可能性がある。

[!plantUML(file='./uml/arch.puml',format='svg',width='640px')]


## Auth
認証・認可サービス

Emailとパスワードで認証する場合の概要図は以下となる。

[!plantUML(file='./uml/auth.puml',format='svg',width='640px')]

## State
アプリケーションコンテキスト管理

Stateのモデルはステートフルなアプリケーションサービスの実現に利用される。

ModelのSessionIDはステートフルなアプリケーションサービスの利用者ごとのアクセスを判別するために使用され、その有効性はSessionIDStatusの状態によって判定される。

アプリケーションサービスは、UseCaseとして上記を管理するリポジトリを利用するクエリ・コマンドをバインドしたApplicationContextManagerを生成して、ステートの取得・保存などの操作を行う。

[!plantUML(file='./uml/state.UseCase.puml',format='svg',width='640px')]

[!plantUML(file='./uml/state.Model.puml',format='svg',width='640px')]

## Auth Infra

InMemoryMapをDBとして作成したAuthサービスの概要は以下のようになる。

[!plantUML(file='./uml/auth.Infra.puml',format='svg',width='640px')]

