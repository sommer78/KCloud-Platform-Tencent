# shellcheck disable=SC2024
cd /opt/laokou && sudo nohup java -Xms256m -Xmx512m -Ddruid.filters=mergeStat -Ddruid.useGlobalDataSourceStat=true -Djava.library.path=/usr/local/apr/lib -Djasypt.encryptor.password=koushenhai5201314wumeihua -Denv=DEV -Dapollo.configService=http://124.222.196.51:8080 -jar admin.jar --spring.profiles.active=prod > admin.log &