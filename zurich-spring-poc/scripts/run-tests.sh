#!/bin/bash

# Zurich Spring POC - Automated Testing Suite
# Bash Automation Script for Unix/Linux CI/CD Integration
# Version: 1.0
# Author: Zurich Development Team

set -euo pipefail

# Script Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
REPORT_PATH="target/test-reports"
TEST_TYPE="unit"
COVERAGE=false
FAIL_FAST=false
PARALLEL=false
VERBOSE=false
PROFILE=""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging Functions
log() {
    local level="$1"
    shift
    local message="$*"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    echo -e "[$timestamp] [$level] $message"
    echo "[$timestamp] [$level] $message" >> "$REPORT_PATH/test-execution.log"
}

log_info() {
    echo -e "${BLUE}$*${NC}"
    log "INFO" "$*"
}

log_success() {
    echo -e "${GREEN}$*${NC}"
    log "SUCCESS" "$*"
}

log_warning() {
    echo -e "${YELLOW}$*${NC}"
    log "WARNING" "$*"
}

log_error() {
    echo -e "${RED}$*${NC}" >&2
    log "ERROR" "$*"
}

# Help function
show_help() {
    cat << EOF
Zurich Spring POC - Automated Testing Suite

Usage: $0 [OPTIONS]

Options:
    -t, --type TYPE         Test type: unit, integration, regression, smoke, performance, all (default: unit)
    -p, --profile PROFILE   Maven profile to use
    -c, --coverage          Enable code coverage analysis
    -f, --fail-fast         Enable fail-fast mode
    -r, --report-path PATH  Custom report path (default: target/test-reports)
    --parallel              Enable parallel test execution
    -v, --verbose           Enable verbose logging
    -h, --help              Show this help message

Examples:
    $0 -t unit -c                    # Run unit tests with coverage
    $0 -t integration --parallel     # Run integration tests in parallel
    $0 -t regression -f              # Run regression tests with fail-fast
    $0 -t all -c --parallel          # Run all tests with coverage and parallel execution

EOF
}

# Parse command line arguments
parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -t|--type)
                TEST_TYPE="$2"
                shift 2
                ;;
            -p|--profile)
                PROFILE="$2"
                shift 2
                ;;
            -c|--coverage)
                COVERAGE=true
                shift
                ;;
            -f|--fail-fast)
                FAIL_FAST=true
                shift
                ;;
            -r|--report-path)
                REPORT_PATH="$2"
                shift 2
                ;;
            --parallel)
                PARALLEL=true
                shift
                ;;
            -v|--verbose)
                VERBOSE=true
                shift
                ;;
            -h|--help)
                show_help
                exit 0
                ;;
            *)
                log_error "Unknown option: $1"
                show_help
                exit 1
                ;;
        esac
    done
}

# Initialize test environment
initialize_test_environment() {
    log_info "Initializing test environment..."
    
    # Create report directory
    mkdir -p "$REPORT_PATH"
    
    # Clean previous test results
    find "$REPORT_PATH" -name "*.xml" -o -name "*.html" -o -name "*.json" | xargs rm -f 2>/dev/null || true
    
    # Verify Java and Maven
    if ! java -version >/dev/null 2>&1; then
        log_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    if ! mvn -version >/dev/null 2>&1; then
        log_error "Maven is not installed or not in PATH"
        exit 1
    fi
    
    local java_version=$(java -version 2>&1 | head -n 1)
    local maven_version=$(mvn -version 2>&1 | head -n 1)
    
    log_info "Java: $java_version"
    log_info "Maven: $maven_version"
}

# Execute tests based on type
execute_tests() {
    local test_category="$1"
    
    log_info "Executing $test_category tests..."
    
    local maven_args=()
    
    case "${test_category,,}" in
        unit)
            maven_args+=(clean test -Punit-tests)
            log_info "Running unit tests only"
            ;;
        integration)
            maven_args+=(clean verify -Pintegration-tests)
            log_info "Running integration tests"
            ;;
        regression)
            maven_args+=(clean test -Pregression-tests)
            log_info "Running regression tests"
            ;;
        smoke)
            maven_args+=(clean test -Psmoke-tests)
            log_info "Running smoke tests"
            ;;
        performance)
            maven_args+=(clean test -Pperformance-tests)
            log_info "Running performance tests"
            ;;
        all)
            maven_args+=(clean verify -Pall-tests)
            log_info "Running all tests"
            ;;
        *)
            log_error "Unknown test type: $test_category"
            exit 1
            ;;
    esac
    
    # Add coverage if requested
    if [[ "$COVERAGE" == true ]]; then
        log_info "Code coverage analysis enabled"
    fi
    
    # Add fail-fast if requested
    if [[ "$FAIL_FAST" == true ]]; then
        maven_args+=(-Dmaven.test.failure.ignore=false)
        log_info "Fail-fast mode enabled"
    fi
    
    # Add parallel execution if requested
    if [[ "$PARALLEL" == true ]]; then
        maven_args+=(-Dmaven.test.parallel=true)
        log_info "Parallel execution enabled"
    fi
    
    # Add custom profile if specified
    if [[ -n "$PROFILE" ]]; then
        maven_args+=(-P"$PROFILE")
        log_info "Custom profile: $PROFILE"
    fi
    
    # Add verbose logging if requested
    if [[ "$VERBOSE" == true ]]; then
        maven_args+=(-X)
        log_info "Verbose logging enabled"
    fi
    
    # Execute Maven command
    local maven_command="mvn ${maven_args[*]}"
    log_info "Executing: $maven_command"
    
    if mvn "${maven_args[@]}"; then
        log_success "$test_category tests completed successfully"
        return 0
    else
        log_error "$test_category tests failed"
        return 1
    fi
}

# Generate test report summary
generate_test_report() {
    log_info "Generating test report summary..."
    
    local end_time=$(date '+%Y-%m-%d %H:%M:%S')
    local start_time_epoch=$START_TIME_EPOCH
    local end_time_epoch=$(date +%s)
    local duration=$((end_time_epoch - start_time_epoch))
    
    # Generate JSON report
    cat > "$REPORT_PATH/test-summary.json" << EOF
{
    "startTime": "$START_TIME",
    "endTime": "$end_time",
    "duration": "$duration seconds",
    "testType": "$TEST_TYPE",
    "success": $TEST_SUCCESS,
    "reportPath": "$REPORT_PATH"
}
EOF
    
    # Generate HTML report
    cat > "$REPORT_PATH/test-report.html" << EOF
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
        <p><strong>Test Type:</strong> $TEST_TYPE</p>
        <p><strong>Start Time:</strong> $START_TIME</p>
        <p><strong>End Time:</strong> $end_time</p>
        <p><strong>Duration:</strong> $duration seconds</p>
        <p><strong>Status:</strong> <span class="$([ "$TEST_SUCCESS" == "true" ] && echo "success" || echo "error")">$([ "$TEST_SUCCESS" == "true" ] && echo "PASSED" || echo "FAILED")</span></p>
    </div>
    
    <h2>Test Results</h2>
    <p>Detailed results can be found in the target/surefire-reports and target/failsafe-reports directories.</p>
    
    <h2>Coverage Report</h2>
    <p>Code coverage reports are available in target/site/jacoco/index.html</p>
</body>
</html>
EOF
    
    log_success "Test report generated at: $REPORT_PATH/test-report.html"
}

# Main execution
main() {
    START_TIME=$(date '+%Y-%m-%d %H:%M:%S')
    START_TIME_EPOCH=$(date +%s)
    
    parse_arguments "$@"
    
    log_info "Starting Zurich Spring POC automated testing..."
    log_info "Test Type: $TEST_TYPE"
    log_info "Coverage: $COVERAGE"
    log_info "Parallel: $PARALLEL"
    log_info "Report Path: $REPORT_PATH"
    
    # Change to project directory
    cd "$PROJECT_ROOT"
    
    # Initialize environment
    initialize_test_environment
    
    # Execute tests
    if execute_tests "$TEST_TYPE"; then
        TEST_SUCCESS=true
        log_success "All tests completed successfully!"
        exit_code=0
    else
        TEST_SUCCESS=false
        log_error "Some tests failed!"
        exit_code=1
    fi
    
    # Generate reports
    generate_test_report
    
    local end_time_epoch=$(date +%s)
    local total_duration=$((end_time_epoch - START_TIME_EPOCH))
    log_info "Test execution completed in $total_duration seconds"
    
    exit $exit_code
}

# Execute main function
main "$@"