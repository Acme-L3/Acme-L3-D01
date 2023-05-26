
package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLecturePublishTest extends TestHarness {

	@Override
	@BeforeAll
	public void beforeAll() {
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();

		super.clickOnButton("Create Course");
		super.fillInputBoxIn("code", "ZZZ999");
		super.fillInputBoxIn("title", "titleTest");
		super.fillInputBoxIn("retailPrice", "EUR 12");
		super.fillInputBoxIn("abstractText", "abstractTest");
		super.fillInputBoxIn("courseType", "BALANCED");
		super.clickOnSubmit("Create");

		super.checkListingExists();

		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("List lectures in the course");
		super.checkListingExists();
		super.clickOnButton("Create Lecture");
		super.checkFormExists();
		super.fillInputBoxIn("title", "ZZZtitle");
		super.fillInputBoxIn("body", "body");
		super.fillInputBoxIn("abstractText", "abstractText");
		super.fillInputBoxIn("lectureType", "THEORY");
		super.fillInputBoxIn("estimateLearningTime", "2.00");
		super.clickOnSubmit("Create");

		super.signOut();
	}
	@Override
	@BeforeEach
	public void beforeEach() {
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();

		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("List lectures in the course");
		super.checkListingExists();
		super.clickOnButton("Create Lecture");
		super.checkFormExists();
		super.fillInputBoxIn("title", "title");
		super.fillInputBoxIn("body", "body");
		super.fillInputBoxIn("abstractText", "abstractText");
		super.fillInputBoxIn("lectureType", "THEORY");
		super.fillInputBoxIn("estimateLearningTime", "2.00");
		super.clickOnSubmit("Create");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String body, final String estimateLearningTime, final String abstractText, final String lectureType, final String link) {
		// HINT: this test authenticates as a lecturer and then lists his or her
		// HINT: courses, creates a new one, and check that it's been created properly.

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();

		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("List lectures in the course");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("abstractText", abstractText);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("estimateLearningTime", estimateLearningTime);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();

		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("List lectures in the course");
		super.checkListingExists();
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 2, body);
		super.checkColumnHasValue(recordIndex, 1, abstractText);
		super.checkColumnHasValue(recordIndex, 3, lectureType);
		super.checkColumnHasValue(recordIndex, 4, estimateLearningTime);
		super.checkColumnHasValue(recordIndex, 5, link);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkNotButtonExists("Delete");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String body, final String estimateLearningTime, final String abstractText, final String lectureType, final String link) {
		// HINT: this test authenticates as a lecturer and then lists his or her
		// HINT: courses, creates a new one, and check that it's been created properly.

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();

		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("List lectures in the course");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("abstractText", abstractText);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("estimateLearningTime", estimateLearningTime);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a tutorials using principals with
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/update");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/update");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/update");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture/update");
		super.checkPanicExists();
		super.signOut();
	}

}
