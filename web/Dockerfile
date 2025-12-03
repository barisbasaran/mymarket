# syntax=docker/dockerfile:1.7-labs

FROM nginx:latest

COPY ./nginx.conf /etc/nginx/

RUN mkdir -p "/web-home"

COPY --exclude=**/uploads ./static/ /web-home/
WORKDIR "/web-home"
RUN mkdir -p "/web-home/uploads"
