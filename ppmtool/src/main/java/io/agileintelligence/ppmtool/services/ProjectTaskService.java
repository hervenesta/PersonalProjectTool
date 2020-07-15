package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
public class ProjectTaskService {

   @Autowired
    private BacklogRepository backlogRepository;

   @Autowired
    private ProjectTaskRepository projectTaskRepository;

   @Autowired
    private ProjectRepository projectRepository;

   public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
       try {
           //Project TASK TO BE ADDED to a specific project, project is not null, and the backlog exists
           Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

           // set the backlog to project tASK
           projectTask.setBacklog(backlog);

           // structure of the project sequence should be like this: ID01-1, ID02-2...
           Integer BacklogSequence = backlog.getPTSequence();

           //update the BL SEQUENCE
           BacklogSequence++;

           backlog.setPTSequence(BacklogSequence);

           // ADD SEQUENCE TO PROJECT TASK
           projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);

           //set the pt identifier
           projectTask.setProjectIdentifier(projectIdentifier);


           //initial status when status is null
           if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
               projectTask.setStatus("TO_DO");
           }

           //INItial priority when priority null
           if (projectTask.getPriority() == 0 || projectTask.getPriority() == null) { // in the future we need projectTask.getPriority() ==0 to handle the form
               projectTask.setPriority(3);
           }
           return projectTaskRepository.save(projectTask);
       }catch(Exception e){
           throw new ProjectNotFoundException("Project not found");
       }
   }

   public Iterable<ProjectTask> findBacklogById(String id){
       Project project = projectRepository.findByProjectIdentifier(id);

       if(project == null){
           throw new ProjectNotFoundException("Project with ID "+id+" does not exist.");
       }
       return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
   }

   public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){
       //make sure we are searching on an existing backlog
       Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
       if(backlog == null){
           throw new ProjectNotFoundException("Project with ID '"+backlog_id+"' does not exist.");
       }
       // make sure that task exists
       ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
       if(projectTask == null){
           throw new ProjectNotFoundException("Project with ID '"+pt_id+"' not found.");
       }
       //make sure that the backlog/project id in the path corresponds to the right project

       if(!projectTask.getProjectIdentifier().equals(backlog_id)){
           throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project '"+backlog_id+"'");
       }
       return projectTask;
   }

   public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
       ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
       ProjectTask projectTask1 = updatedTask;
       return projectTaskRepository.save(projectTask1);
   }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
       ProjectTask projectTask  = findPTByProjectSequence(backlog_id, pt_id);
//       Backlog backlog = projectTask.getBacklog();
//       List<ProjectTask> pts = backlog.getProjectTasks();
//       pts.remove(projectTask);
//       backlogRepository.save(backlog);
       projectTaskRepository.delete(projectTask);
    }

}
