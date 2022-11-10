FROM sebp/elk:7.10.1

RUN rm /etc/logstash/conf.d/30-output.conf
COPY ./logstash-config/output.conf /etc/logstash/conf.d/30-output.conf

RUN rm /etc/logstash/conf.d/02-beats-input.conf
COPY ./logstash-config/beats-input.conf /etc/logstash/conf.d/02-beats-input.conf
