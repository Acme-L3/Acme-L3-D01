
package acme.features.student.enrolment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entitites.course.Course;
import acme.entitites.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentRepository repo;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Enrolment object;
		Principal principal;
		int enrolmentId;

		enrolmentId = super.getRequest().getData("id", int.class);
		object = this.repo.findEnrolmentById(enrolmentId);
		principal = super.getRequest().getPrincipal();

		status = object.getStudent().getId() == principal.getActiveRoleId() && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repo.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		Course course;

		course = this.repo.findCourseById(object.getCourse().getId());

		super.bind(object, "code", "motivation", "goals");
		object.setCourse(course);
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment exist;
			exist = this.repo.findEnrolmentByCode(object.getCode());
			super.state(exist == null || exist.equals(object), "code", "student.enrolment.error.code");

		}
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repo.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;

		final Course course = object.getCourse();

		tuple = super.unbind(object, "code", "motivation", "goals", "lowerNibble", "holderName", "draftMode");
		tuple.put("courseShow", course.getCode());

		super.getResponse().setData(tuple);
	}

}
