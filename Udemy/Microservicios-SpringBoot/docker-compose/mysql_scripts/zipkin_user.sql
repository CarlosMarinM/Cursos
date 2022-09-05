CREATE USER 'zipkin'@'%' IDENTIFIED BY 'zipkin';
GRANT Select ON zipkin.* TO 'zipkin'@'%';
GRANT Show view ON zipkin.* TO 'zipkin'@'%';
GRANT Update ON zipkin.* TO 'zipkin'@'%';
GRANT Insert ON zipkin.* TO 'zipkin'@'%';
GRANT Delete ON zipkin.* TO 'zipkin'@'%';
GRANT Execute ON zipkin.* TO 'zipkin'@'%';
