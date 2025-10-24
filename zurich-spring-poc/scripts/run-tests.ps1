# Zurich Spring POC - Automated Testing Suite
# PowerShell Automation Script for CI/CD Integration
# Version: 1.0
# Author: Zurich Development Team

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("unit", "integration", "regression", "smoke", "performance", "all")]
    [string]$TestType = "unit",
    
    [Parameter(Mandatory=$false)]
    [string]$Profile = "",
    
    [Parameter(Mandatory=$false)]
    [switch]$Coverage = $false,
    
    [Parameter(Mandatory=$false)]
    [switch]$FailFast = $false,
    
    [Parameter(Mandatory=$false)]
    [string]$ReportPath = "target/test-reports",
    
    [Parameter(Mandatory=$false)]
    [switch]$Parallel = $false,
    
    [Parameter(Mandatory=$false)]
    [switch]$Verbose = $false
)

# Script Configuration
$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path $MyInvocation.MyCommand.Path
$TestResults = @()
$StartTime = Get-Date

# Logging Functions
function Write-TestLog {
    param($Message, $Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logMessage = "[$timestamp] [$Level] $Message"
    Write-Host $logMessage
    Add-Content -Path "$ReportPath/test-execution.log" -Value $logMessage
}

function Write-Success {
    param($Message)
    Write-Host $Message -ForegroundColor Green
    Write-TestLog $Message "SUCCESS"
}

function Write-Error {
    param($Message)
    Write-Host $Message -ForegroundColor Red
    Write-TestLog $Message "ERROR"
}

function Write-Warning {
    param($Message)
    Write-Host $Message -ForegroundColor Yellow
    Write-TestLog $Message "WARNING"
}

# Initialize Test Environment
function Initialize-TestEnvironment {
    Write-TestLog "Initializing test environment..."
    
    # Create report directory
    if (-not (Test-Path $ReportPath)) {
        New-Item -ItemType Directory -Path $ReportPath -Force | Out-Null
    }
    
    # Clean previous test results
    Get-ChildItem -Path $ReportPath -Include "*.xml", "*.html", "*.json" -Recurse | Remove-Item -Force
    
    # Verify Java and Maven
    try {
        $javaVersion = java -version 2>&1 | Select-String "version"
        Write-TestLog "Java: $javaVersion"
        
        $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
        Write-TestLog "Maven: $mavenVersion"
    }
    catch {
        Write-Error "Failed to verify Java/Maven installation: $_"
        exit 1
    }
}

# Execute Tests Based on Type
function Execute-Tests {
    param($TestCategory)
    
    Write-TestLog "Executing $TestCategory tests..."
    
    $mavenArgs = @()
    
    switch ($TestCategory.ToLower()) {
        "unit" {
            $mavenArgs += @("clean", "test", "-Punit-tests")
            Write-TestLog "Running unit tests only"
        }
        "integration" {
            $mavenArgs += @("clean", "verify", "-Pintegration-tests")
            Write-TestLog "Running integration tests"
        }
        "regression" {
            $mavenArgs += @("clean", "test", "-Pregression-tests")
            Write-TestLog "Running regression tests"
        }
        "smoke" {
            $mavenArgs += @("clean", "test", "-Psmoke-tests")
            Write-TestLog "Running smoke tests"
        }
        "performance" {
            $mavenArgs += @("clean", "test", "-Pperformance-tests")
            Write-TestLog "Running performance tests"
        }
        "all" {
            $mavenArgs += @("clean", "verify", "-Pall-tests")
            Write-TestLog "Running all tests"
        }
        default {
            Write-Error "Unknown test type: $TestCategory"
            exit 1
        }
    }
    
    # Add coverage if requested
    if ($Coverage) {
        Write-TestLog "Code coverage analysis enabled"
    }
    
    # Add fail-fast if requested
    if ($FailFast) {
        $mavenArgs += "-Dmaven.test.failure.ignore=false"
        Write-TestLog "Fail-fast mode enabled"
    }
    
    # Add parallel execution if requested
    if ($Parallel) {
        $mavenArgs += "-Dmaven.test.parallel=true"
        Write-TestLog "Parallel execution enabled"
    }
    
    # Add custom profile if specified
    if ($Profile) {
        $mavenArgs += "-P$Profile"
        Write-TestLog "Custom profile: $Profile"
    }
    
    # Add verbose logging if requested
    if ($Verbose) {
        $mavenArgs += "-X"
        Write-TestLog "Verbose logging enabled"
    }
    
    # Execute Maven command
    try {
        $mavenCommand = "mvn " + ($mavenArgs -join " ")
        Write-TestLog "Executing: $mavenCommand"
        
        $process = Start-Process -FilePath "mvn" -ArgumentList $mavenArgs -NoNewWindow -PassThru -Wait
        
        if ($process.ExitCode -eq 0) {
            Write-Success "$TestCategory tests completed successfully"
            return $true
        } else {
            Write-Error "$TestCategory tests failed with exit code: $($process.ExitCode)"
            return $false
        }
    }
    catch {
        Write-Error "Failed to execute Maven command: $_"
        return $false
    }
}

# Generate Test Report Summary
function Generate-TestReport {
    Write-TestLog "Generating test report summary..."
    
    $endTime = Get-Date
    $duration = $endTime - $StartTime
    
    $reportSummary = @{
        StartTime = $StartTime
        EndTime = $endTime
        Duration = $duration
        TestType = $TestType
        Success = $TestResults -contains $true
        ReportPath = $ReportPath
    }
    
    $reportJson = $reportSummary | ConvertTo-Json -Depth 3
    $reportJson | Out-File -FilePath "$ReportPath/test-summary.json"
    
    # Generate HTML report
    $htmlReport = @"
<!DOCTYPE html>
<html>
<head>
    <title>Zurich Spring POC - Test Execution Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background-color: #f0f0f0; padding: 10px; border-radius: 5px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Test Execution Report</h1>
        <p><strong>Test Type:</strong> $TestType</p>
        <p><strong>Start Time:</strong> $($StartTime.ToString('yyyy-MM-dd HH:mm:ss'))</p>
        <p><strong>End Time:</strong> $($endTime.ToString('yyyy-MM-dd HH:mm:ss'))</p>
        <p><strong>Duration:</strong> $($duration.ToString('hh\:mm\:ss'))</p>
        <p><strong>Status:</strong> <span class="$(if($TestResults -contains $true) {'success'} else {'error'})">$(if($TestResults -contains $true) {'PASSED'} else {'FAILED'})</span></p>
    </div>
    
    <h2>Test Results</h2>
    <p>Detailed results can be found in the target/surefire-reports and target/failsafe-reports directories.</p>
    
    <h2>Coverage Report</h2>
    <p>Code coverage reports are available in target/site/jacoco/index.html</p>
</body>
</html>
"@
    
    $htmlReport | Out-File -FilePath "$ReportPath/test-report.html"
    
    Write-Success "Test report generated at: $ReportPath/test-report.html"
}

# Main Execution
function Main {
    Write-TestLog "Starting Zurich Spring POC automated testing..."
    Write-TestLog "Test Type: $TestType"
    Write-TestLog "Coverage: $Coverage"
    Write-TestLog "Parallel: $Parallel"
    Write-TestLog "Report Path: $ReportPath"
    
    try {
        # Initialize environment
        Initialize-TestEnvironment
        
        # Change to project directory
        Set-Location $ProjectRoot
        
        # Execute tests
        $testResult = Execute-Tests -TestCategory $TestType
        $TestResults += $testResult
        
        # Generate reports
        Generate-TestReport
        
        if ($testResult) {
            Write-Success "All tests completed successfully!"
            exit 0
        } else {
            Write-Error "Some tests failed!"
            exit 1
        }
    }
    catch {
        Write-Error "Test execution failed: $_"
        exit 1
    }
    finally {
        Write-TestLog "Test execution completed in $((Get-Date) - $StartTime)"
    }
}

# Execute main function
Main