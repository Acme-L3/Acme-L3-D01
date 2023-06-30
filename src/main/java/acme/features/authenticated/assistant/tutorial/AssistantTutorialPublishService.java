
package acme.features.authenticated.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entitites.course.Course;
import acme.entitites.session.TutorialSession;
import acme.entitites.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final int tutorialId;
		final Tutorial tutorial;
		final Assistant assistant;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(assistant);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		super.bind(object, "code", "tittle", "summary", "goals");
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial existing;
			existing = this.repository.findTutorialByCode(object.getCode());
			super.state(existing == null || existing.getId() == object.getId(), "code", "assistant.tutorial.form.error.duplicated-code");
		}

		if (!super.getBuffer().getErrors().hasErrors("*")) {
			final Collection<TutorialSession> theory = this.repository.findTutorialSessionsByTutorialId(object.getId());
			super.state(!theory.isEmpty(), "*", "assistant.tutorial.form.error.publish.empty");
		}

	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Collection<Course> courses;
		final SelectChoices coursesChoices;
		final Tuple tuple;

		final Collection<TutorialSession> sessions;
		Double estimatedTime;
		sessions = this.repository.findTutorialSessionsByTutorialId(object.getId());
		estimatedTime = 0.;
		for (final TutorialSession ts : sessions)
			estimatedTime += ts.getHoursFromPeriod();

		courses = this.repository.findAllCourses();
		coursesChoices = SelectChoices.from(courses, "title", object.getCourse());
		tuple = super.unbind(object, "code", "tittle", "summary", "goals", "draftMode");
		tuple.put("estimatedTime", estimatedTime);
		tuple.put("course", coursesChoices.getSelected().getKey());
		tuple.put("courses", coursesChoices);

		super.getResponse().setData(tuple);
	}
}
