param(
    [switch]$SoloCompilar
)

$ErrorActionPreference = "Stop"

$raizProyecto = Split-Path -Parent $MyInvocation.MyCommand.Path
$directorioSalida = Join-Path $raizProyecto "out"

function Buscar-EjecutableJava {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Nombre
    )

    if ($env:JAVA_HOME) {
        $rutaJavaHome = Join-Path $env:JAVA_HOME "bin\$Nombre"
        if (Test-Path $rutaJavaHome) {
            return $rutaJavaHome
        }
    }

    $ejecutable = Get-Command $Nombre -ErrorAction SilentlyContinue
    if ($ejecutable) {
        return $ejecutable.Source
    }

    $rutasComunes = @(
        "C:\Program Files\Android\openjdk",
        "C:\Program Files\Eclipse Adoptium",
        "C:\Program Files\Java"
    )

    foreach ($rutaBase in $rutasComunes) {
        if (-not (Test-Path $rutaBase)) {
            continue
        }

        $candidatos = @((Join-Path $rutaBase "bin\$Nombre"))
        $candidatos += Get-ChildItem -Path $rutaBase -Directory -ErrorAction SilentlyContinue |
                Sort-Object Name -Descending |
                ForEach-Object { Join-Path $_.FullName "bin\$Nombre" }

        foreach ($candidato in $candidatos) {
            if (Test-Path $candidato) {
                return $candidato
            }
        }
    }

    throw "No se encontro $Nombre. Instala un JDK 11 o superior, o configura JAVA_HOME."
}

$javac = Buscar-EjecutableJava "javac.exe"
$java = Buscar-EjecutableJava "java.exe"

if (-not (Test-Path $directorioSalida)) {
    New-Item -ItemType Directory -Path $directorioSalida | Out-Null
}

$archivosFuente = @(Get-ChildItem -Path (Join-Path $raizProyecto "src") -Filter "*.java" | ForEach-Object {
    $_.FullName
})

if ($archivosFuente.Count -eq 0) {
    throw "No se encontraron archivos .java en la carpeta src."
}

& $javac -encoding UTF-8 -d $directorioSalida $archivosFuente

if (-not $SoloCompilar) {
    Push-Location $raizProyecto
    try {
        & $java -cp $directorioSalida Principal
    } finally {
        Pop-Location
    }
}
