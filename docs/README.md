# UML

```
./gradlew docs:render
```

## Architecture
簡易的なOnion Architectureを採用する。

Stateは、アプリケーションの状態管理を担うため、他のドメインから横断的に利用される可能性がある。

<img src="https://raw.githubusercontent.com/aya-eiya/gradle-vertx-sample/master/docs/generated/arch.svg" data-type="svg" style="width:640px">


## Auth
認証・認可サービス

Emailとパスワードで認証するInMemoryMaoをDBとして作成した場合の概要図は以下となる。

<img src="https://raw.githubusercontent.com/aya-eiya/gradle-vertx-sample/master/docs/generated/auth.svg" data-type="svg" style="width:640px">

## State
アプリケーションコンテキスト管理

Stateのモデルはステートフルなアプリケーションサービスの実現に利用される。

ModelのSessionIDはステートフルなアプリケーションサービスの利用者ごとのアクセスを判別するために使用され、その有効性はSessionIDStatusの状態によって判定される。

アプリケーションサービスは、UseCaseとして上記を管理するリポジトリを利用するクエリ・コマンドをバインドしたApplicationContextManagerを生成して、ステートの取得・保存などの操作を行う。

<img src="https://raw.githubusercontent.com/aya-eiya/gradle-vertx-sample/master/docs/generated/state.UseCase.svg" data-type="svg" style="width:640px">

<img src="https://raw.githubusercontent.com/aya-eiya/gradle-vertx-sample/master/docs/generated/state.Model.svg" data-type="svg" style="width:640px">

## Auth Infra
<img src="https://raw.githubusercontent.com/aya-eiya/gradle-vertx-sample/master/docs/generated/auth.Infra.svg" data-type="svg" style="width:640px">

