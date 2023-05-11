<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>


<acme:form>
	<acme:input-textbox code="authenticated.practicum.form.label.practicum-code" path="code"/>	
	<acme:input-textbox code="authenticated.practicum.form.label.title" path="title"/>
	<acme:input-textbox code="authenticated.practicum.form.label.summary" path="summary"/>
	<acme:input-textbox code="authenticated.practicum.form.label.goals" path="goals"/>
	<acme:input-textbox code="authenticated.practicum.form.label.company-name" path="companyName"/>
	<acme:input-textbox code="authenticated.practicum.form.label.VATnumber" path="VATnumber"/>
	<acme:input-textbox code="authenticated.practicum.form.label.company-summary" path="companySummary"/>
	<acme:input-url code="authenticated.practicum.form.label.link" path="link"/>
</acme:form>
