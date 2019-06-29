# jNMS

В качестве курсовго проекта вместо интернет-магазина предлагается разработка enterprise-системы "Network inventory & management". Наличие таких систем характерно для компаний, занимающихся эксплуатацией больших сетей передачи данных. Некоторые компании используют вендор-специфические системы (Junos Space, Huawei U2000/M2000 и т.д.), но, как правило, сети, построенные на оборудовании разных вендоров объединяются под "зонтичными" системами управления. В более мелких компаниях обычно используются готовые open-source решения (Zabbix, Nagios и т.д.), в более крупных - коммерческие решения, такие как NetCracker, IBM Network manager и т.д, при этом настраиваются механизмы интеграции для оперативного обмена данными между такими системами.

В качестве курсового проекта предлагается создать упрощенный аналог системы Nagios, обладающий следующими основными функциями: 
1. **Inventory**. Позволяет хранить информацию о работающем сетевом оборудовании - сайт (площадка, где установлено оборудование), вендор, модель, серийный номер и т.д. Степень детализации предметной области предлагается определить в процессе работы над проектом. При наличии достаточного количества времени, детализация может быть увеличена. Например, можно добавить хранение информации о физической топологии сети - т.е. какие порты как и чем соединены между собой.
2. **Management**. Обеспечивает сбор информации в реальном времени. Для простоты будем использовать только протокол ICMP. Система будет хранить не только текущее состояние, но историю изменения состояний, что позволит формировать простые отчеты.
3. **Notifications**. Позволяет пользователям системы получать оперативные уведомления о текущем состоянии сети - наличии аварий, восстановлении и т.д. Пользователи должны иметь возможность формировать подписки на события исходя из своей роли в системе и из степени критичности того или иного события. 

#### Предлагаемая архитектура реализации
Система состоит из трех независимых приложений, завязанных на общую базу данных. Взаимодействие между приложениями осуществляется через собственно БД, а также AMQP. 
1. **Web-подсистема**. Реализует функции Inventory, предоставляет доступ к данным через web-интерфейс и REST API. Содержит 2 основных раздела - "админку" и "общий". Админка позволяет непосредственно управлять наполнением системы - выполнять CRUD-операции над сущностями, хранящимися в БД, через web-интерфейс, интегрирующийся с back-end-ом при помощи thymeleaf. "Общий" раздел позволяет просматривать текущее состояние сети и соответствующие события. Возможна интеграция с Google Maps API или аналогами для более наглядного отображения. Интеграция с back-end-ом осуществляется при помощи REST API, используется "ванильный" JS (по возможности в минимальном количестве).
2. **Мониторинг**.  Реализуется "демонами", которые осуществляют периодический обход сети в соответствии со своими настройками, степенью "приоритетности" тех или иных узлов сети, а также их текущим состоянием (с авариями/без аварий). Информация пишется в БД, при этом в случае, если обнаружено изменение состояния узла, отправляются сообщения в web и notifier. Web-подсистема может отреагировать например путем "зажигания красной лампочки" в web-интерфейсе через механизм web-сокетов, а notifier отправит сообщения заинтересованным пользователям.
3. **Notifier**. Это пассивная подсистема, реагирующая на сообщения, приходящие через AMQP. На основании информации из БД вычисляется список рассылки для того или иного события и способы, которыми сообщение должно быть доставлено тому или иному пользователю. После этого осуществляется непосредственно рассылка сообщения. 