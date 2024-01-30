# kafkalab3
請試著佈署一個kafka cluster，並完成下列步驟：
1. 建立一個具有 10 個 partitions 的 topic
2. 使用官方 Java Producer APIs 撰寫一隻程式傳遞 10 筆資料給上述 topic
3. 使用官方 Java Consumer APIs 撰寫一隻程式從上述 topic 收取該10筆資料
4. 請試著讓步驟2 和 步驟 3 的資料順序一致。例如 producer 傳送的資料順序為 a,b,c,d，consumer 收到的資料應該為 a,b,c,d

## 說明
### 解法一：規定Producer 送到同一partition (Consumer不改動)
#### Producer v1
[Producer v1](src/main/java/kafka/myproducer/example)
#### Consumer v0&1
[Consumer v0&1](src/main/java/kafka/myconsumer)

### 解法二：替資料加上順序label
:::info
待補
:::

## 備註
第一次寫Java，對整個Java與Maven不熟悉，之後會將整個專案調整好
