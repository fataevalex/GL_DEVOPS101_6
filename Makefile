.PHONY: all

all:  image push

APP=$(shell basename $(shell git remote get-url origin | tr '[:upper:]' '[:lower:]'))
REGESTRY=fataevalex
REPO=github.com/fataevalex/gl_devops101_6
CURRENTARCH=$(shell  shell go env GOARCH)
VERSION=$(shell git describe --tags --abbrev=0 || echo "v1.0.0")-$(shell git rev-parse HEAD|cut -c1-7)
HELM_VERSION=$(shell git describe --tags --abbrev=0)

TARGETOS=linux
TARGETARCH=arm64

format:
	gofmt -s -w ./

lint: format
	golint

test: lint
	go test -v

build: format
	CGO_ENABLED=0 GOOS=${TARGETOS} GOARCH=${CURRENTARCH} go build -v -o kbot -ldflags "-X="${REPO}/cmd.appVersion=${VERSION}

image:
	docker build . -t ${REGESTRY}/${APP}:${VERSION}-${TARGETARCH} --no-cache --build-arg TARGETOS=${TARGETOS} --build-arg TARGETARCH=${TARGETARCH} --build-arg REPO=${REPO}

push:
	docker push ${REGESTRY}/${APP}:${VERSION}-${TARGETARCH}

helm-package:
	helm package helm/ --version ${HELM_VERSION} --app-version ${VERSION}

helm-push:
	helm push ${APP}-${HELM_VERSION}.tgz oci://ghcr.io/${REGESTRY}/charts

clean:
	rm -rf kbot
	rm -f ${APP}-*.tgz