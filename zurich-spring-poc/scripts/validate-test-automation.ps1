# Zurich Spring POC - Test Automation Validation Script
# Validates the automated testing setup and configuration
# Version: 1.0

param(
    [Parameter(Mandatory=$false)]
    [switch]$QuickValidation = $false,
    
    [Parameter(Mandatory=$false)]
    [switch]$FullValidation = $false,
    
    [Parameter(Mandatory=$false)]
    [switch]$Verbose = $false
)

$ErrorActionPreference = "Stop"
$ValidationResults = @()

function Write-ValidationLog {
    param($Message, $Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    if ($Verbose -or $Level -ne "DEBUG") {
        Write-Host "[$timestamp] [$Level] $Message" -ForegroundColor $(
            switch($Level) {
                "ERROR" { "Red" }
                "WARNING" { "Yellow" }
                "SUCCESS" { "Green" }
                default { "White" }
            }
        )
    }
}

function Test-Prerequisite {
    param($Name, $Command, $ExpectedOutput = "")
    
    Write-ValidationLog "Validating $Name..." "INFO"
    
    try {
        $result = Invoke-Expression $Command
        if ($ExpectedOutput -and $result -notmatch $ExpectedOutput) {
            throw "Unexpected output: $result"
        }
        
        Write-ValidationLog "$Name validation passed" "SUCCESS"
        return $true
    }
    catch {
        Write-ValidationLog "$Name validation failed: $_" "ERROR"
        return $false
    }
}

function Test-FileExists {
    param($FilePath, $Description)
    
    Write-ValidationLog "Checking $Description..." "DEBUG"
    
    if (Test-Path $FilePath) {
        Write-ValidationLog "$Description exists" "SUCCESS"
        return $true
    }
    else {
        Write-ValidationLog "$Description not found at: $FilePath" "ERROR"
        return $false
    }
}

function Test-MavenProfile {
    param($ProfileName)
    
    Write-ValidationLog "Validating Maven profile: $ProfileName..." "INFO"
    
    try {
        $result = mvn help:active-profiles -P$ProfileName 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-ValidationLog "Maven profile '$ProfileName' is valid" "SUCCESS"
            return $true
        }
        else {
            throw "Profile validation failed"
        }
    }
    catch {
        Write-ValidationLog "Maven profile '$ProfileName' validation failed: $_" "ERROR"
        return $false
    }
}

function Test-TestExecution {
    param($TestType)
    
    Write-ValidationLog "Testing $TestType execution..." "INFO"
    
    try {
        $scriptPath = ".\scripts\run-tests.ps1"
        if (-not (Test-Path $scriptPath)) {
            throw "Test script not found"
        }
        
        # Run a quick test to validate execution
        $result = & $scriptPath -TestType $TestType -Verbose:$false
        
        if ($LASTEXITCODE -eq 0) {
            Write-ValidationLog "$TestType execution test passed" "SUCCESS"
            return $true
        }
        else {
            throw "Test execution failed with exit code: $LASTEXITCODE"
        }
    }
    catch {
        Write-ValidationLog "$TestType execution test failed: $_" "ERROR"
        return $false
    }
}

function Validate-Prerequisites {
    Write-ValidationLog "=== Validating Prerequisites ===" "INFO"
    
    $results = @()
    
    # Java validation
    $results += Test-Prerequisite "Java 21" "java -version 2>&1 | Select-String 'version'" "21"
    
    # Maven validation
    $results += Test-Prerequisite "Maven" "mvn -version 2>&1 | Select-String 'Apache Maven'"
    
    # Git validation
    $results += Test-Prerequisite "Git" "git --version"
    
    return $results -notcontains $false
}

function Validate-ProjectStructure {
    Write-ValidationLog "=== Validating Project Structure ===" "INFO"
    
    $results = @()
    
    # Core project files
    $results += Test-FileExists "pom.xml" "Maven POM file"
    $results += Test-FileExists "src\main\java" "Main source directory"
    $results += Test-FileExists "src\test\java" "Test source directory"
    
    # Automation scripts
    $results += Test-FileExists "scripts\run-tests.ps1" "PowerShell test script"
    $results += Test-FileExists "scripts\run-tests.sh" "Bash test script"
    
    # Configuration files
    $results += Test-FileExists "src\test\resources\application-regression-test.properties" "Regression test configuration"
    $results += Test-FileExists ".github\workflows\automated-testing.yml" "CI/CD workflow"
    
    # Documentation
    $results += Test-FileExists "TESTING.md" "Testing documentation"
    
    return $results -notcontains $false
}

function Validate-MavenConfiguration {
    Write-ValidationLog "=== Validating Maven Configuration ===" "INFO"
    
    $results = @()
    
    # Test Maven profiles
    $profiles = @("unit-tests", "integration-tests", "regression-tests", "smoke-tests", "performance-tests", "all-tests")
    
    foreach ($profile in $profiles) {
        $results += Test-MavenProfile $profile
    }
    
    return $results -notcontains $false
}

function Validate-TestExecution {
    Write-ValidationLog "=== Validating Test Execution ===" "INFO"
    
    if ($QuickValidation) {
        Write-ValidationLog "Quick validation mode - skipping actual test execution" "WARNING"
        return $true
    }
    
    $results = @()
    
    # Test different test types
    $testTypes = @("unit")  # Start with unit tests only for validation
    
    if ($FullValidation) {
        $testTypes += @("integration", "smoke")
    }
    
    foreach ($testType in $testTypes) {
        $results += Test-TestExecution $testType
    }
    
    return $results -notcontains $false
}

function Validate-TestReporting {
    Write-ValidationLog "=== Validating Test Reporting ===" "INFO"
    
    $results = @()
    
    # Check if report directories exist or can be created
    $reportDirs = @("target\test-reports", "target\surefire-reports", "target\site\jacoco")
    
    foreach ($dir in $reportDirs) {
        try {
            if (-not (Test-Path $dir)) {
                New-Item -ItemType Directory -Path $dir -Force | Out-Null
            }
            $results += $true
            Write-ValidationLog "Report directory validated: $dir" "SUCCESS"
        }
        catch {
            Write-ValidationLog "Failed to create report directory: $dir" "ERROR"
            $results += $false
        }
    }
    
    return $results -notcontains $false
}

function Generate-ValidationReport {
    Write-ValidationLog "=== Generating Validation Report ===" "INFO"
    
    $reportContent = @"
# Test Automation Validation Report

**Generated**: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
**Validation Type**: $(if($QuickValidation) { 'Quick' } elseif($FullValidation) { 'Full' } else { 'Standard' })

## Validation Results

$(foreach($result in $ValidationResults) { "- $($result.Category): $($result.Status)`n" })

## Summary

- **Total Checks**: $($ValidationResults.Count)
- **Passed**: $($ValidationResults | Where-Object { $_.Status -eq 'PASSED' } | Measure-Object | Select-Object -ExpandProperty Count)
- **Failed**: $($ValidationResults | Where-Object { $_.Status -eq 'FAILED' } | Measure-Object | Select-Object -ExpandProperty Count)

## Next Steps

$(if ($ValidationResults | Where-Object { $_.Status -eq 'FAILED' }) {
    "⚠️  **Action Required**: Some validations failed. Please review the failures above and resolve them before proceeding with automated testing."
} else {
    "✅ **Ready**: All validations passed. The automated testing suite is ready for use."
})

---
*Generated by Zurich Spring POC Test Automation Validation Script*
"@
    
    # Save report to file
    $reportPath = "target\validation-report.md"
    if (-not (Test-Path "target")) {
        New-Item -ItemType Directory -Path "target" -Force | Out-Null
    }
    
    $reportContent | Out-File -FilePath $reportPath -Encoding UTF8
    Write-ValidationLog "Validation report saved to: $reportPath" "SUCCESS"
}

function Main {
    Write-ValidationLog "Starting Test Automation Validation..." "INFO"
    Write-ValidationLog "Validation Mode: $(if($QuickValidation) { 'Quick' } elseif($FullValidation) { 'Full' } else { 'Standard' })" "INFO"
    
    $overallSuccess = $true
    
    # Run validation steps
    $prereqResult = Validate-Prerequisites
    $ValidationResults += @{ Category = "Prerequisites"; Status = $(if($prereqResult) { "PASSED" } else { "FAILED" }) }
    $overallSuccess = $overallSuccess -and $prereqResult
    
    $structureResult = Validate-ProjectStructure
    $ValidationResults += @{ Category = "Project Structure"; Status = $(if($structureResult) { "PASSED" } else { "FAILED" }) }
    $overallSuccess = $overallSuccess -and $structureResult
    
    $mavenResult = Validate-MavenConfiguration
    $ValidationResults += @{ Category = "Maven Configuration"; Status = $(if($mavenResult) { "PASSED" } else { "FAILED" }) }
    $overallSuccess = $overallSuccess -and $mavenResult
    
    $executionResult = Validate-TestExecution
    $ValidationResults += @{ Category = "Test Execution"; Status = $(if($executionResult) { "PASSED" } else { "FAILED" }) }
    $overallSuccess = $overallSuccess -and $executionResult
    
    $reportingResult = Validate-TestReporting
    $ValidationResults += @{ Category = "Test Reporting"; Status = $(if($reportingResult) { "PASSED" } else { "FAILED" }) }
    $overallSuccess = $overallSuccess -and $reportingResult
    
    # Generate validation report
    Generate-ValidationReport
    
    # Final summary
    Write-ValidationLog "=== Validation Complete ===" "INFO"
    
    if ($overallSuccess) {
        Write-ValidationLog "🎉 All validations passed! Test automation suite is ready." "SUCCESS"
        exit 0
    }
    else {
        Write-ValidationLog "❌ Some validations failed. Please review and fix the issues." "ERROR"
        exit 1
    }
}

# Execute main function
Main