# 1) Создать папку lib, если её нет
New-Item -ItemType Directory -Force -Path lib

# 2) Указать корень вашего локального репозитория Maven
$repo = "$env:USERPROFILE\.m2\repository"

# 3) Список путей до нужных JAR-ов внутри ~/.m2/repository
$files = @(
  "jakarta/annotation/jakarta.annotation-api/3.0.0/jakarta.annotation-api-3.0.0.jar",
  "jakarta/el/jakarta.el-api/6.0.0/jakarta.el-api-6.0.0.jar",
  "jakarta/enterprise/jakarta.enterprise.cdi-api/4.1.0/jakarta.enterprise.cdi-api-4.1.0.jar",
  "jakarta/enterprise/jakarta.enterprise.lang-model/4.1.0/jakarta.enterprise.lang-model-4.1.0.jar",
  "jakarta/faces/jakarta.faces-api/4.1.0/jakarta.faces-api-4.1.0.jar",
  "jakarta/inject/jakarta.inject-api/2.0.1/jakarta.inject-api-2.0.1.jar",
  "jakarta/interceptor/jakarta.interceptor-api/2.2.0/jakarta.interceptor-api-2.2.0.jar",
  "jakarta/servlet/jakarta.servlet-api/6.1.0/jakarta.servlet-api-6.1.0.jar",
  "org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar",
  "org/checkerframework/checker-qual/3.42.0/checker-qual-3.42.0.jar",
  "org/junit/jupiter/junit-jupiter-api/5.9.2/junit-jupiter-api-5.9.2.jar",
  "org/junit/jupiter/junit-jupiter-engine/5.9.2/junit-jupiter-engine-5.9.2.jar",
  "org/junit/platform/junit-platform-commons/1.9.2/junit-platform-commons-1.9.2.jar",
  "org/junit/platform/junit-platform-engine/1.9.2/junit-platform-engine-1.9.2.jar",
  "org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar",
  "org/postgresql/postgresql/42.7.4/postgresql-42.7.4.jar",
  "org/primefaces/primefaces/13.0.0/primefaces-13.0.0.jar"
)

# 4) Копируем
foreach($relative in $files) {
  $src = Join-Path $repo $relative
  if (Test-Path $src) {
    Copy-Item $src -Destination lib -Force
    Write-Host "Copied $relative"
  } else {
    Write-Warning "Not found: $relative"
  }
}
