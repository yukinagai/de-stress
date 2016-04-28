ハードウェアコントローラー
====
ハードウェア側をコントロールするためのnodeサーバー

## How to setup
````
npm install node-serial
node a
````

## プロペラコントロール機能
* HTTPリクエストを送るとプロペラを回す
* リクエスト例：http://localhost:8001/motor/[power]
* powerは0-255

## 温度通知機能
* Arduinoから温度が通知されると、サーバーに温度を送る
* http://157.7.242.70/bath/temperature/39.5


## ページ操作
* http://localhost:8001/page/fan
* プロペラ操作ページへ遷移 
* http://localhost:8001/page/temp
* 温度操作ページへ遷移

