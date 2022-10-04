/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.job.steps;

import com.google.inject.Singleton;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.step.JobStep;
import org.junit.Assert;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class JobServiceSteps extends JobServiceTestBase {

    private JobFactory jobFactory;
    private JobService jobService;

    @Inject
    public JobServiceSteps(StepData stepData) {
        super(stepData);
    }

    @Before(value = "@env_docker or @env_docker_base or @env_none", order = 10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();

        jobService = locator.getService(JobService.class);
        jobFactory = locator.getFactory(JobFactory.class);
    }

    @When("I configure the job service")
    public void setJobServiceConfigurationValue(List<CucConfig> cucConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getCurrentScopeId();
        KapuaId scopeId = getCurrentParentId();

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getParentId() != null) {
                scopeId = getKapuaId(config.getParentId());
            }
            if (config.getScopeId() != null) {
                accId = getKapuaId(config.getScopeId());
            }
        }

        primeException();
        try {
            jobService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("A regular job creator with the name {string}")
    public void prepareARegularJobCreator(String name) {
        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName(name);
        jobCreator.setDescription(TEST_JOB);
        stepData.put(JOB_CREATOR, jobCreator);
    }

    @Given("A job creator with a null name")
    public void prepareAJobCreatorWithNullName() {
        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName(null);
        jobCreator.setDescription(TEST_JOB);
        stepData.put(JOB_CREATOR, jobCreator);
    }

    @Given("A job creator with an empty name")
    public void prepareAJobCreatorWithAnEmptyName() {
        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName("");
        jobCreator.setDescription(TEST_JOB);
        stepData.put(JOB_CREATOR, jobCreator);
    }

    @Given("I create a job with the name {string}")
    public void createANamedJob(String name) throws Exception {
        prepareARegularJobCreator(name);
        JobCreator jobCreator = (JobCreator) stepData.get(JOB_CREATOR);
        primeException();
        try {
            stepData.remove("Job");
            stepData.remove(CURRENT_JOB_ID);
            Job job = jobService.create(jobCreator);
            stepData.put("Job", job);
            stepData.put(CURRENT_JOB_ID, job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I create {int} job items")
    public void createANumberOfJobs(int num) throws Exception {
        primeException();
        try {
            for (int i = 0; i < num; i++) {
                JobCreator tmpCreator = jobFactory.newCreator(getCurrentScopeId());
                tmpCreator.setName(String.format("TestJobNum%d", i));
                tmpCreator.setDescription("TestJobDescription");
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I create {int} job items with the name {string}")
    public void createANumberOfJobsWithName(int num, String name) throws Exception {
        JobCreator tmpCreator = jobFactory.newCreator(getCurrentScopeId());
        tmpCreator.setDescription("TestJobDescription");
        primeException();
        try {
            for (int i = 0; i < num; i++) {
                tmpCreator.setName(name + "_" + i);
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I create a new job entity from the existing creator")
    public void createJobFromCreator() throws Exception {
        JobCreator jobCreator = (JobCreator) stepData.get(JOB_CREATOR);
        primeException();
        try {
            stepData.remove("Job");
            stepData.remove(CURRENT_JOB_ID);
            Job job = jobService.create(jobCreator);
            stepData.put("Job", job);
            stepData.put(CURRENT_JOB_ID, job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I change the job name to {string}")
    public void updateExistingJobName(String newName) throws Exception {
        Job oldJob = (Job) stepData.get("Job");
        oldJob.setName(newName);
        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I change the job description to {string}")
    public void updateExistingJobDescription(String newDescription) throws Exception {
        Job oldJob = (Job) stepData.get("Job");
        oldJob.setDescription(newDescription);
        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I change the job XML definition to {string}")
    public void updateExistingJobXMLDefinition(String newDefinition) throws Exception {
        Job oldJob = (Job) stepData.get("Job");
        oldJob.setJobXmlDefinition(newDefinition);
        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I add the current step to the last job")
    public void updateJobWithSteps() throws Exception {
        Job oldJob = (Job) stepData.get("Job");
        List<JobStep> tmpStepList = oldJob.getJobSteps();
        JobStep step = (JobStep) stepData.get("Step");
        tmpStepList.add(step);
        oldJob.setJobSteps(tmpStepList);
        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the job")
    public void deleteJobFromDatabase() throws Exception {
        Job job = (Job) stepData.get("Job");
        primeException();
        try {
            jobService.delete(job.getScopeId(), job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for the job in the database")
    public void findJobInDatabase() throws Exception {
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        primeException();
        try {
            stepData.remove("Job");
            Job job = jobService.find(getCurrentScopeId(), currentJobId);
            stepData.put("Job", job);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I count the jobs in the database")
    public void countJobsInDatabase() throws Exception {
        updateCount(() -> (int) jobService.count(jobFactory.newQuery(getCurrentScopeId())));
    }

    @When("I query for jobs in scope {int}")
    public void countJobsInScope(int id) throws Exception {
        updateCount(() -> jobService.query(jobFactory.newQuery(getKapuaId(id))).getSize());
    }

    @When("I count the jobs with the name starting with {string}")
    public void countJobsWithName(String name) throws Exception {
        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, name, Operator.STARTS_WITH));
        updateCount(() -> jobService.query(tmpQuery).getSize());
    }

    @When("I query for the job with the name {string}")
    public void queryForJobWithName(String name) throws Exception {
        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, name));
        primeException();
        try {
            stepData.remove("Job");
            Job job = jobService.query(tmpQuery).getFirstItem();
            stepData.put("Job", job);
            Assert.assertEquals(name, job.getName());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The job entity matches the creator")
    public void checkJobAgainstCreator() {
        Job job = (Job) stepData.get("Job");
        JobCreator jobCreator = (JobCreator) stepData.get(JOB_CREATOR);
        Assert.assertEquals("The job scope does not match the creator.", jobCreator.getScopeId(), job.getScopeId());
        Assert.assertEquals("The job name does not match the creator.", jobCreator.getName(), job.getName());
        Assert.assertEquals("The job description does not match the creator.", jobCreator.getDescription(), job.getDescription());
    }

    @Then("The job has int step(s)")
    public void checkNumberOfJobSteps(int num) {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals("The job item has the wrong number of steps", num, job.getJobSteps().size());
    }

    @Then("I find a job item in the database")
    public void checkThatAJobWasFound() {
        Assert.assertNotNull("Unexpected null value for the job.", stepData.get("Job"));
    }

    @Then("There is no such job item in the database")
    public void checkThatNoJobWasFound() {
        Assert.assertNull("Unexpected job item was found!", stepData.get("Job"));
    }

    @Then("The job name is {string}")
    public void checkJobItemName(String name) {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals("The job name does not match!", name, job.getName());
    }

    @Then("The job description is {string}")
    public void checkJobItemDescription(String description) {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals("The job description does not match!", description, job.getDescription());
    }

    @Then("The job XML definition is {string}")
    public void checkJobItemXMLDefinition(String definition) {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals("The job XML definition does not match!", definition, job.getJobXmlDefinition());
    }

    @When("I test the sanity of the job factory")
    public void testJobFactorySanity() {
        primeException();
        Assert.assertNotNull("The job factory returned a null creator!", jobFactory.newCreator(SYS_SCOPE_ID));
        Assert.assertNotNull("The job factory returned a null job object!", jobFactory.newEntity(SYS_SCOPE_ID));
        Assert.assertNotNull("The job factory returned a null job query!", jobFactory.newQuery(SYS_SCOPE_ID));
        Assert.assertNotNull("The job factory returned a null job list result!", jobFactory.newListResult());
    }

    @Then("I find a job with name {string}")
    public void iFindAJobWithName(String jobName) {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals(job.getName(), jobName);
    }

    @Then("I try to delete the job with name {string}")
    public void iDeleteTheJobWithName(String jobName) throws Exception {
        Job job = (Job) stepData.get("Job");
        try {
            primeException();
            if (job.getName().equals(jobName)) {
                jobService.delete(getCurrentScopeId(), job.getId());
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("I try to edit job to name {string}")
    public void iTryToEditJobToName(String jobName) throws Throwable {
        Job job = (Job) stepData.get("Job");
        job.setName(jobName);
        try {
            primeException();
            Job newJob = jobService.update(job);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }


    @When("I query for the job with the name {string} and I find it")
    public void iQueryForTheJobWithTheNameAndIFoundIt(String jobName) throws Exception {
        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, jobName));
        primeException();
        try {
            stepData.remove("Job");
            Job job = jobService.query(tmpQuery).getFirstItem();
            stepData.put("Job", job);
            Assert.assertEquals(jobName, job.getName());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I prepare a job with name {string} and description {string}")
    public void iPrepareAJobWithNameAndDescription(String name, String description) {
        JobCreator jobCreator = jobFactory.newCreator(SYS_SCOPE_ID);
        jobCreator.setName(name);
        jobCreator.setDescription(description);
        stepData.put(JOB_CREATOR, jobCreator);
    }

    @When("I try to create job with permitted symbols {string} in name")
    public void iTryToCreateJobWithPermittedSymbolsInName(String validCharacters) throws Exception {
        tryToCreateJob(validCharacters);
    }

    @When("I try to create job with invalid symbols {string} in name")
    public void iTryToCreateJobWithInvalidSymbolsInName(String invalidCharacters) throws Exception {
        tryToCreateJob(invalidCharacters);
    }

    @Then("I find a job with description {string}")
    public void iFindAJobWithDescription(String jobDescription) throws Throwable {
        Job job = (Job) stepData.get("Job");
        Assert.assertEquals(job.getDescription(), jobDescription);
    }

    @Then("I try to update job name with permitted symbols {string} in name")
    public void iTryToUpdateJobNameWithPermittedSymbolsInName(String validCharacters) throws Exception {
        tryToUpdateJobName(validCharacters);
    }

    @When("I try to update job name with invalid symbols {string} in name")
    public void iTryToUpdateJobNameWithInvalidSymbolsInName(String invalidCharacters) throws Exception {
        tryToUpdateJobName(invalidCharacters);
    }

    @Then("I change name of job from {string} to {string}")
    public void iChangeNameOfJobFromTo(String oldName, String newName) throws Throwable {
        try {
            JobQuery query = jobFactory.newQuery(getCurrentScopeId());
            query.setPredicate(query.attributePredicate(JobAttributes.NAME, oldName, Operator.EQUAL));
            JobListResult queryResult = jobService.query(query);
            Job job = queryResult.getFirstItem();
            job.setName(newName);
            jobService.update(job);
            stepData.put("Job", job);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("There is no job with name {string} in database")
    public void thereIsNoJobWithNameInDatabase(String jobName) {
        Job job = (Job) stepData.get("Job");
        Assert.assertNotEquals(job.getName(), jobName);
    }

    @When("I change the job description from {string} to {string}")
    public void iChangeTheJobDescriptionFromTo(String oldDescription, String newDescription) throws Throwable {
        try {
            JobQuery query = jobFactory.newQuery(getCurrentScopeId());
            query.setPredicate(query.attributePredicate(JobAttributes.DESCRIPTION, oldDescription, Operator.EQUAL));
            JobListResult queryResult = jobService.query(query);
            Job job = queryResult.getFirstItem();
            job.setDescription(newDescription);
            jobService.update(job);
            stepData.put("Job", job);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    //
    // Private methods
    //

    private void tryToCreateJob(String characters) throws Exception {
        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        for (int i = 0; i < characters.length(); i++) {
            String jobName = JOB_NAME + characters.charAt(i);
            jobCreator.setName(jobName);
            try {
                primeException();
                Job job = jobService.create(jobCreator);
                stepData.put("Job", job);
                stepData.put(CURRENT_JOB_ID, job.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    private void tryToUpdateJobName(String characters) throws Exception {
        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        //are we sure works as expected with invalid characters?
        for (int i = 0; i < characters.length(); i++) {
            String jobName = JOB_NAME + characters.charAt(i);
            jobCreator.setName(JOB_NAME + i);
            try {
                primeException();
                stepData.remove("Job");
                Job job = jobService.create(jobCreator);
                job.setName(jobName);
                jobService.update(job);
                stepData.put(CURRENT_JOB_ID, job.getId());
                stepData.put("Job", job);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

}
