# vertx+ScalaのGradleプロジェクトビルド検証

Gradleが色々変わったので、ビルドを検証してみる

https://qiita.com/aya_eiya/items/e9eb2306e93772d4ba95

のある意味続き

## Gradleのサブコマンドでプロジェクト生成する

```
gradle init
```

から初めてみた。

対話形式でプロジェクトの構成を設定する。

前回はシングルプロジェクトとして作成したので、今回はサブプロジェクトを複数含むビルドを試したい。

選択は以下のようにした

```
% gradle init                                                              h_ayabe@aya-eiya-mb-air-2

Welcome to Gradle 6.7!

Select type of project to generate:
  2: application
Enter selection (default: basic) [1..4] 2

Select implementation language:
  5: Scala
Enter selection (default: Java) [1..6] 5

Split functionality across multiple subprojects?:
  2: yes - application and library projects
Enter selection (default: no - only one application project) [1..2] 2

Select build script DSL:
  1: Groovy
Enter selection (default: Groovy) [1..2] 1
```

## ビルド環境の設定

今回のプロジェクトは`Scala 2.12.x`でビルドしたい。

デフォルトだと`13.0.0`が使用されてしまうので、どこで設定しているのか探してみたがかなり迷った。

`buildSrc/src/main/groovy/${projectName}.scala-common-conventions.gradle`

だった。

`dependencies.implementation`を

```gradle
  implementation 'org.scala-lang:scala-library:2.12.12'
```

あたりに書き換える。

## compiler pluginを使用する

Scalaではコンパイラープラグインを使用して、Syntaxを追加したり文法解釈を変更したりできる。

今回は、[typelevel/kind-projector](https://github.com/typelevel/kind-projector) を使用して、GradleでCompilerPluginが使用できるかどうか検証してみる。

このプラグインは、いくつかのSyntaxを追加する。

設定方法は、ScalaのVersion設定と同様に、

`buildSrc/src/main/groovy/${projectName}.scala-common-conventions.gradle`

`dependencies.implementation`に、

```gradle
    scalaCompilerPlugins "org.typelevel:kind-projector_${scalaVersionFull}:0.11.0"
    compile "org.typelevel:cats-core_${scalaVersion}:2.2.0"
```

と記述する。

## テストエンジンにScalaTestを使う

上記ファイルに、
```

    // Use JUnit Jupiter API & ScalaTest for testing.
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.+',
                       "org.scalatest:scalatest_${scalaVersion}:3.2.+"

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine',
                    'co.helmethair:scalatest-junit-runner:0.1.8'
```

を追加する。

## 永続化手段はとりあえずInMemoryで行う

まずは適当に実装していくので、InMemoryのMapを永続化手段として使用する。

ただし、後で入れ替えられるようにレイヤーは考えておく。


