package com.zurich.poc.model;

import com.zurich.poc.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PolicySummary model class
 * Implements KAN-1 requirement for comprehensive unit testing
 * Tests KAN-13 implementation for policy holder gender field
 * Tests KAN-24 implementation for nominee information fields
 * Tests KAN-25 implementation for address information fields
 */
@DisplayName("PolicySummary Model Tests")
class PolicySummaryTest {

    @Nested
    @DisplayName("KAN-13: Gender Field Tests")
    class GenderFieldTests {

        @Test
        @DisplayName("Should create PolicySummary with gender field")
        void shouldCreatePolicySummaryWithGender() {
            // Given
            String expectedGender = "Female";
            
            // When
            PolicySummary policy = new PolicySummary();
            policy.setGender(expectedGender);
            
            // Then
            assertThat(policy.getGender()).isEqualTo(expectedGender);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Male", "Female", "Non-binary", "Prefer not to say", ""})
        @DisplayName("Should accept various gender values")
        void shouldAcceptVariousGenderValues(String gender) {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withGender(gender)
                    .build();
            
            // Then
            assertThat(policy.getGender()).isEqualTo(gender);
        }

        @Test
        @DisplayName("Should handle null gender value")
        void shouldHandleNullGenderValue() {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withGender(null)
                    .build();
            
            // Then
            assertThat(policy.getGender()).isNull();
        }

        @Test
        @DisplayName("Should create male policy holder using builder")
        void shouldCreateMalePolicyHolderUsingBuilder() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .male()
                    .withPolicyHolderName("John Smith")
                    .build();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Male");
            assertThat(policy.getPolicyHolderName()).isEqualTo("John Smith");
        }

        @Test
        @DisplayName("Should create female policy holder using builder")
        void shouldCreateFemalePolicyHolderUsingBuilder() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .female()
                    .withPolicyHolderName("Jane Doe")
                    .build();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Female");
            assertThat(policy.getPolicyHolderName()).isEqualTo("Jane Doe");
        }

        @Test
        @DisplayName("Should create non-binary policy holder using builder")
        void shouldCreateNonBinaryPolicyHolderUsingBuilder() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .nonBinary()
                    .withPolicyHolderName("Alex Johnson")
                    .build();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Non-binary");
            assertThat(policy.getPolicyHolderName()).isEqualTo("Alex Johnson");
        }

        @Test
        @DisplayName("Should use factory method for male policy holder")
        void shouldUseFactoryMethodForMalePolicyHolder() {
            // When
            PolicySummary policy = TestDataBuilder.createMalePolicyHolder();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Male");
            assertThat(policy.getPolicyHolderName()).isEqualTo("John Smith");
            assertThat(policy.getEmail()).isEqualTo("john.smith@example.com");
        }

        @Test
        @DisplayName("Should use factory method for female policy holder")
        void shouldUseFactoryMethodForFemalePolicyHolder() {
            // When
            PolicySummary policy = TestDataBuilder.createFemalePolicyHolder();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Female");
            assertThat(policy.getPolicyHolderName()).isEqualTo("Jane Doe");
            assertThat(policy.getEmail()).isEqualTo("jane.doe@example.com");
        }

        @Test
        @DisplayName("Should use factory method for non-binary policy holder")
        void shouldUseFactoryMethodForNonBinaryPolicyHolder() {
            // When
            PolicySummary policy = TestDataBuilder.createNonBinaryPolicyHolder();
            
            // Then
            assertThat(policy.getGender()).isEqualTo("Non-binary");
            assertThat(policy.getPolicyHolderName()).isEqualTo("Alex Johnson");
            assertThat(policy.getEmail()).isEqualTo("alex.johnson@example.com");
        }
    }

    @Nested
    @DisplayName("KAN-24: Nominee Information Tests")
    class NomineeInformationTests {

        @Test
        @DisplayName("Should create PolicySummary with nominee information")
        void shouldCreatePolicySummaryWithNomineeInformation() {
            // Given
            String nomineeName = "Alice Johnson";
            String nomineeRelationship = "Spouse";
            String nomineeContactInfo = "alice.johnson@example.com";
            String nomineeIdentification = "ID123456789";
            
            // When
            PolicySummary policy = new PolicySummary();
            policy.setNomineeName(nomineeName);
            policy.setNomineeRelationship(nomineeRelationship);
            policy.setNomineeContactInfo(nomineeContactInfo);
            policy.setNomineeIdentification(nomineeIdentification);
            
            // Then
            assertThat(policy.getNomineeName()).isEqualTo(nomineeName);
            assertThat(policy.getNomineeRelationship()).isEqualTo(nomineeRelationship);
            assertThat(policy.getNomineeContactInfo()).isEqualTo(nomineeContactInfo);
            assertThat(policy.getNomineeIdentification()).isEqualTo(nomineeIdentification);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Spouse", "Child", "Parent", "Sibling", "Friend", "Other"})
        @DisplayName("Should accept various nominee relationship values")
        void shouldAcceptVariousNomineeRelationshipValues(String relationship) {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withNomineeRelationship(relationship)
                    .build();
            
            // Then
            assertThat(policy.getNomineeRelationship()).isEqualTo(relationship);
        }

        @Test
        @DisplayName("Should handle null nominee values")
        void shouldHandleNullNomineeValues() {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withNomineeName(null)
                    .withNomineeRelationship(null)
                    .withNomineeContactInfo(null)
                    .withNomineeIdentification(null)
                    .build();
            
            // Then
            assertThat(policy.getNomineeName()).isNull();
            assertThat(policy.getNomineeRelationship()).isNull();
            assertThat(policy.getNomineeContactInfo()).isNull();
            assertThat(policy.getNomineeIdentification()).isNull();
        }

        @Test
        @DisplayName("Should create policy with complete nominee information using builder")
        void shouldCreatePolicyWithCompleteNomineeInformationUsingBuilder() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyHolderName("John Smith")
                    .withNomineeName("Mary Smith")
                    .withNomineeRelationship("Spouse")
                    .withNomineeContactInfo("mary.smith@example.com")
                    .withNomineeIdentification("ID987654321")
                    .build();
            
            // Then
            assertThat(policy.getPolicyHolderName()).isEqualTo("John Smith");
            assertThat(policy.getNomineeName()).isEqualTo("Mary Smith");
            assertThat(policy.getNomineeRelationship()).isEqualTo("Spouse");
            assertThat(policy.getNomineeContactInfo()).isEqualTo("mary.smith@example.com");
            assertThat(policy.getNomineeIdentification()).isEqualTo("ID987654321");
        }

        @Test
        @DisplayName("Should create policy with nominee as child")
        void shouldCreatePolicyWithNomineeAsChild() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyHolderName("Sarah Johnson")
                    .withNomineeName("Emma Johnson")
                    .withNomineeRelationship("Child")
                    .withNomineeContactInfo("emma.johnson@example.com")
                    .build();
            
            // Then
            assertThat(policy.getNomineeName()).isEqualTo("Emma Johnson");
            assertThat(policy.getNomineeRelationship()).isEqualTo("Child");
        }
    }

    @Nested
    @DisplayName("KAN-25: Address Information Tests")
    class AddressInformationTests {

        @Test
        @DisplayName("Should create PolicySummary with address information")
        void shouldCreatePolicySummaryWithAddressInformation() {
            // Given
            String streetAddress = "123 Main Street";
            String city = "New York";
            String state = "NY";
            String postalCode = "10001";
            String country = "USA";
            String addressType = "Residential";
            
            // When
            PolicySummary policy = new PolicySummary();
            policy.setStreetAddress(streetAddress);
            policy.setCity(city);
            policy.setState(state);
            policy.setPostalCode(postalCode);
            policy.setCountry(country);
            policy.setAddressType(addressType);
            
            // Then
            assertThat(policy.getStreetAddress()).isEqualTo(streetAddress);
            assertThat(policy.getCity()).isEqualTo(city);
            assertThat(policy.getState()).isEqualTo(state);
            assertThat(policy.getPostalCode()).isEqualTo(postalCode);
            assertThat(policy.getCountry()).isEqualTo(country);
            assertThat(policy.getAddressType()).isEqualTo(addressType);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Residential", "Business", "Mailing", "Permanent", "Temporary"})
        @DisplayName("Should accept various address type values")
        void shouldAcceptVariousAddressTypeValues(String addressType) {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withAddressType(addressType)
                    .build();
            
            // Then
            assertThat(policy.getAddressType()).isEqualTo(addressType);
        }

        @Test
        @DisplayName("Should handle null address values")
        void shouldHandleNullAddressValues() {
            // Given & When
            PolicySummary policy = TestDataBuilder.policy()
                    .withStreetAddress(null)
                    .withCity(null)
                    .withState(null)
                    .withPostalCode(null)
                    .withCountry(null)
                    .withAddressType(null)
                    .build();
            
            // Then
            assertThat(policy.getStreetAddress()).isNull();
            assertThat(policy.getCity()).isNull();
            assertThat(policy.getState()).isNull();
            assertThat(policy.getPostalCode()).isNull();
            assertThat(policy.getCountry()).isNull();
            assertThat(policy.getAddressType()).isNull();
        }

        @Test
        @DisplayName("Should create policy with complete address information using builder")
        void shouldCreatePolicyWithCompleteAddressInformationUsingBuilder() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyHolderName("John Smith")
                    .withStreetAddress("456 Oak Avenue")
                    .withCity("Boston")
                    .withState("MA")
                    .withPostalCode("02101")
                    .withCountry("USA")
                    .withAddressType("Residential")
                    .build();
            
            // Then
            assertThat(policy.getPolicyHolderName()).isEqualTo("John Smith");
            assertThat(policy.getStreetAddress()).isEqualTo("456 Oak Avenue");
            assertThat(policy.getCity()).isEqualTo("Boston");
            assertThat(policy.getState()).isEqualTo("MA");
            assertThat(policy.getPostalCode()).isEqualTo("02101");
            assertThat(policy.getCountry()).isEqualTo("USA");
            assertThat(policy.getAddressType()).isEqualTo("Residential");
        }

        @Test
        @DisplayName("Should create policy with business address")
        void shouldCreatePolicyWithBusinessAddress() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyHolderName("ABC Corporation")
                    .withStreetAddress("100 Business Plaza")
                    .withCity("Chicago")
                    .withState("IL")
                    .withPostalCode("60601")
                    .withCountry("USA")
                    .withAddressType("Business")
                    .build();
            
            // Then
            assertThat(policy.getStreetAddress()).isEqualTo("100 Business Plaza");
            assertThat(policy.getCity()).isEqualTo("Chicago");
            assertThat(policy.getAddressType()).isEqualTo("Business");
        }

        @Test
        @DisplayName("Should create policy with international address")
        void shouldCreatePolicyWithInternationalAddress() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyHolderName("International Client")
                    .withStreetAddress("10 Downing Street")
                    .withCity("London")
                    .withState("England")
                    .withPostalCode("SW1A 2AA")
                    .withCountry("United Kingdom")
                    .withAddressType("Residential")
                    .build();
            
            // Then
            assertThat(policy.getStreetAddress()).isEqualTo("10 Downing Street");
            assertThat(policy.getCity()).isEqualTo("London");
            assertThat(policy.getState()).isEqualTo("England");
            assertThat(policy.getPostalCode()).isEqualTo("SW1A 2AA");
            assertThat(policy.getCountry()).isEqualTo("United Kingdom");
        }
    }

    @Nested
    @DisplayName("General PolicySummary Tests")
    class GeneralPolicyTests {

        @Test
        @DisplayName("Should create PolicySummary with all fields")
        void shouldCreatePolicySummaryWithAllFields() {
            // Given
            String policyNumber = "POL-12345";
            String holderName = "Test Holder";
            String email = "test@example.com";
            String gender = "Female";
            String policyType = "Health";
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            LocalDate endDate = LocalDate.of(2025, 1, 1);
            String status = "ACTIVE";
            // KAN-24: Nominee fields
            String nomineeName = "Test Nominee";
            String nomineeRelationship = "Spouse";
            String nomineeContactInfo = "nominee@example.com";
            String nomineeIdentification = "ID123456789";
            // KAN-25: Address fields
            String streetAddress = "789 Test Street";
            String city = "Test City";
            String state = "TS";
            String postalCode = "12345";
            String country = "USA";
            String addressType = "Residential";

            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyNumber(policyNumber)
                    .withPolicyHolderName(holderName)
                    .withEmail(email)
                    .withGender(gender)
                    .withPolicyType(policyType)
                    .withStartDate(startDate)
                    .withEndDate(endDate)
                    .withStatus(status)
                    .withNomineeName(nomineeName)
                    .withNomineeRelationship(nomineeRelationship)
                    .withNomineeContactInfo(nomineeContactInfo)
                    .withNomineeIdentification(nomineeIdentification)
                    .withStreetAddress(streetAddress)
                    .withCity(city)
                    .withState(state)
                    .withPostalCode(postalCode)
                    .withCountry(country)
                    .withAddressType(addressType)
                    .build();

            // Then
            assertThat(policy.getPolicyNumber()).isEqualTo(policyNumber);
            assertThat(policy.getPolicyHolderName()).isEqualTo(holderName);
            assertThat(policy.getEmail()).isEqualTo(email);
            assertThat(policy.getGender()).isEqualTo(gender);
            assertThat(policy.getPolicyType()).isEqualTo(policyType);
            assertThat(policy.getStartDate()).isEqualTo(startDate);
            assertThat(policy.getEndDate()).isEqualTo(endDate);
            assertThat(policy.getStatus()).isEqualTo(status);
            // KAN-24: Assert nominee fields
            assertThat(policy.getNomineeName()).isEqualTo(nomineeName);
            assertThat(policy.getNomineeRelationship()).isEqualTo(nomineeRelationship);
            assertThat(policy.getNomineeContactInfo()).isEqualTo(nomineeContactInfo);
            assertThat(policy.getNomineeIdentification()).isEqualTo(nomineeIdentification);
            // KAN-25: Assert address fields
            assertThat(policy.getStreetAddress()).isEqualTo(streetAddress);
            assertThat(policy.getCity()).isEqualTo(city);
            assertThat(policy.getState()).isEqualTo(state);
            assertThat(policy.getPostalCode()).isEqualTo(postalCode);
            assertThat(policy.getCountry()).isEqualTo(country);
            assertThat(policy.getAddressType()).isEqualTo(addressType);
        }

        @Test
        @DisplayName("Should create default PolicySummary with gender, nominee, and address information")
        void shouldCreateDefaultPolicySummaryWithGenderNomineeAndAddressInformation() {
            // When
            PolicySummary policy = TestDataBuilder.createDefaultPolicy();

            // Then
            assertThat(policy.getPolicyNumber()).isEqualTo("POL-001");
            assertThat(policy.getPolicyHolderName()).isEqualTo("John Doe");
            assertThat(policy.getEmail()).isEqualTo("john.doe@example.com");
            assertThat(policy.getGender()).isEqualTo("Male"); // KAN-13: Default gender
            assertThat(policy.getPolicyType()).isEqualTo("Auto");
            assertThat(policy.getStatus()).isEqualTo("ACTIVE");
            // KAN-24: Assert default nominee information
            assertThat(policy.getNomineeName()).isEqualTo("Jane Doe");
            assertThat(policy.getNomineeRelationship()).isEqualTo("Spouse");
            assertThat(policy.getNomineeContactInfo()).isEqualTo("jane.doe@example.com");
            assertThat(policy.getNomineeIdentification()).isEqualTo("ID123456789");
            // KAN-25: Assert default address information
            assertThat(policy.getStreetAddress()).isEqualTo("123 Main Street");
            assertThat(policy.getCity()).isEqualTo("Springfield");
            assertThat(policy.getState()).isEqualTo("IL");
            assertThat(policy.getPostalCode()).isEqualTo("62701");
            assertThat(policy.getCountry()).isEqualTo("USA");
            assertThat(policy.getAddressType()).isEqualTo("Residential");
        }

        @Test
        @DisplayName("Should support method chaining for builder pattern")
        void shouldSupportMethodChainingForBuilderPattern() {
            // When
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyNumber("POL-999")
                    .female()
                    .active()
                    .withEmail("test@zurich.com")
                    .build();

            // Then
            assertThat(policy.getPolicyNumber()).isEqualTo("POL-999");
            assertThat(policy.getGender()).isEqualTo("Female");
            assertThat(policy.getStatus()).isEqualTo("ACTIVE");
            assertThat(policy.getEmail()).isEqualTo("test@zurich.com");
        }
    }

    @Nested
    @DisplayName("Lombok Integration Tests")
    class LombokIntegrationTests {

        @Test
        @DisplayName("Should generate proper toString including gender, nominee, and address fields")
        void shouldGenerateProperToStringIncludingGenderNomineeAndAddressFields() {
            // Given
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyNumber("POL-TEST")
                    .withGender("Female")
                    .withNomineeName("Jane Nominee")
                    .withNomineeRelationship("Spouse")
                    .withStreetAddress("123 Test Street")
                    .withCity("Test City")
                    .withAddressType("Residential")
                    .build();

            // When
            String toStringResult = policy.toString();

            // Then
            assertThat(toStringResult)
                    .contains("policyNumber=POL-TEST")
                    .contains("gender=Female")
                    .contains("nomineeName=Jane Nominee")
                    .contains("nomineeRelationship=Spouse")
                    .contains("streetAddress=123 Test Street")
                    .contains("city=Test City")
                    .contains("addressType=Residential");
        }

        @Test
        @DisplayName("Should generate proper equals method including gender, nominee, and address fields")
        void shouldGenerateProperEqualsMethodIncludingGenderNomineeAndAddressFields() {
            // Given
            PolicySummary policy1 = TestDataBuilder.policy()
                    .withPolicyNumber("POL-SAME")
                    .withGender("Male")
                    .withNomineeName("John Nominee")
                    .withStreetAddress("123 Same Street")
                    .build();

            PolicySummary policy2 = TestDataBuilder.policy()
                    .withPolicyNumber("POL-SAME")
                    .withGender("Female")
                    .withNomineeName("John Nominee")
                    .withStreetAddress("123 Same Street")
                    .build();

            PolicySummary policy3 = TestDataBuilder.policy()
                    .withPolicyNumber("POL-SAME")
                    .withGender("Male")
                    .withNomineeName("Jane Nominee")
                    .withStreetAddress("123 Same Street")
                    .build();

            PolicySummary policy4 = TestDataBuilder.policy()
                    .withPolicyNumber("POL-SAME")
                    .withGender("Male")
                    .withNomineeName("John Nominee")
                    .withStreetAddress("456 Different Street")
                    .build();

            PolicySummary policy5 = TestDataBuilder.policy()
                    .withPolicyNumber("POL-SAME")
                    .withGender("Male")
                    .withNomineeName("John Nominee")
                    .withStreetAddress("123 Same Street")
                    .build();

            // Then
            assertThat(policy1).isNotEqualTo(policy2); // Different gender
            assertThat(policy1).isNotEqualTo(policy3); // Different nominee
            assertThat(policy1).isNotEqualTo(policy4); // Different address
            assertThat(policy1).isEqualTo(policy5); // Same gender, nominee, and address
        }

        @Test
        @DisplayName("Should generate proper hashCode method including gender, nominee, and address fields")
        void shouldGenerateProperHashCodeMethodIncludingGenderNomineeAndAddressFields() {
            // Given
            PolicySummary policy1 = TestDataBuilder.policy()
                    .withGender("Male")
                    .withNomineeName("John Nominee")
                    .withStreetAddress("123 Main Street")
                    .build();

            PolicySummary policy2 = TestDataBuilder.policy()
                    .withGender("Female")
                    .withNomineeName("Jane Nominee")
                    .withStreetAddress("456 Oak Avenue")
                    .build();

            // Then
            assertThat(policy1.hashCode()).isNotEqualTo(policy2.hashCode());
        }
    }
}