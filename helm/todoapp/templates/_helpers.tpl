{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "service.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Template name.
*/}}
{{- define "service.template" -}}
{{- .Template.Name | replace (printf "%s%s" .Template.BasePath "/") "" | trimSuffix ".yaml" | trunc 63 -}}
{{- end -}}

{{/*
Template name including release name.
*/}}
{{- define "service.templatefull" -}}
{{- printf "%s-%s" .Release.Name .Template.Name | replace (printf "%s%s" .Template.BasePath "/") "" | trimSuffix ".yaml" | trunc 63 -}}
{{- end -}}

{{/*
Template default labels
*/}}
{{- define "service.defaultlabels" -}}
    app.kubernetes.io/name: {{ .Release.Name }}-{{ include "service.template" . }}
    helm.sh/chart: {{ include "service.chart" . }}
    app.kubernetes.io/instance: {{ .Release.Name }}
    app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Template for flowable secret environment variables
*/}}
{{- define "service.listSecretEnvVariables"}}
{{- range $key := .Values.service.secrets.keys }}
- name: SERVICE_SECRET_{{ $key | upper }}
  valueFrom:
    secretKeyRef:
      name: {{ $.Values.service.secrets.name }}
      key: {{ $key }}
{{- end }}
{{- end }}

{{/*
Template for additional secret environment variables
*/}}
{{- define "service.listAdditionalSecretEnvVariables"}}
{{- range $.Values.service.envSecrets }}
- name: {{ .name }}
  valueFrom:
    secretKeyRef:
      name: {{ .secretName }}
      key: {{ .secretKey }}
{{- end }}
{{- end }}


{{/*
Template for secrets mapped to volumes
*/}}
{{- define "service.listVolumeSecrets"}}
{{- range $.Values.service.volumeSecrets }}
- name: {{ .name }}
  secret:
    secretName: {{ .secretName }}
{{- end }}
{{- end }}

{{/*
Template for secrets mapped to volumes (mount part)
*/}}
{{- define "service.listVolumeMountSecrets"}}
{{- range $.Values.service.volumeSecrets }}
- name: {{ .name }}
  mountPath: {{ .mountPath }}
  readOnly: true
{{- end }}
{{- end }}

{{/*
Template for externalized Spring Config volumeMounts
*/}}
{{- define "service.springConfigVolumeMounts"}}
{{- if .enabled }}
{{- if .mountBase }}
- name: spring-config-vol-base
  mountPath: "/app/resources/{{ .springConfigName }}.yml"
  subPath: "{{ .springConfigName }}.yml"
{{- end }}
{{- range $profile := .profiles }}
- name: spring-config-vol-{{ $profile }}
  mountPath: "/app/resources/{{ $.springConfigName }}-{{ $profile }}.yml"
  subPath: "{{ $.springConfigName }}-{{ $profile }}.yml"
{{- end }}
{{- end }}
{{- end }}


{{/*
Template for externalized Spring Config volume

  springConfigMounts:
    enabled: false
    configMapName: ~
    mountBase: false
    profiles: [ ]
    # @see https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files
    springConfigName: 'application'
*/}}
{{- define "service.springConfigVolumes"}}
{{- if .enabled }}
{{- if .mountBase }}
- name: spring-config-vol-base
  configMap:
    name: {{ .configMapName }}
    items:
    - key: '{{ .springConfigName }}.yml'
      path: '{{ .springConfigName}}.yml'
{{- end }}
{{- range $profile := .profiles }}
- name: spring-config-vol-{{ $profile }}
  configMap:
    name: {{ $.configMapName }}
    items:
    - key: '{{ $.springConfigName }}-{{ $profile }}.yml'
      path: '{{ $.springConfigName}}-{{ $profile }}.yml'
{{- end }}
{{- end }}
{{- end }}