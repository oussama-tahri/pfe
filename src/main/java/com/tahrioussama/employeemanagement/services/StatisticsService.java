//package com.tahrioussama.employeemanagement.services;
//
//import com.tahrioussama.employeemanagement.entities.Absence;
//import com.tahrioussama.employeemanagement.entities.Employee;
//import com.tahrioussama.employeemanagement.repositories.AbsenceRepository;
//import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class StatisticsService {
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private AbsenceRepository absenceRepository;
//
//    // Méthode pour calculer le pourcentage d'heures travaillées par rapport aux heures de base
//    public double calculateHoursWorkedPercentage(Employee employee) {
//        // Récupérer le nombre total d'heures travaillées par l'employé
//        int totalHoursWorked = calculateTotalHoursWorked(employee);
//
//        // Calculer le pourcentage d'heures travaillées par rapport aux heures de base
//        int baseHours = 44; // Heures de base par semaine
//        return ((double) totalHoursWorked / baseHours) * 100;
//    }
//
//
//    // Méthode pour calculer le nombre total d'heures travaillées par l'employé
//    private int calculateTotalHoursWorked(Employee employee) {
//        // Récupérer la liste des absences de l'employé
//        List<Absence> absences = absenceRepository.findByEmployee(employee);
//
//        // Initialiser le compteur du nombre total d'heures travaillées
//        int totalHoursWorked = 0;
//
//        // Parcourir chaque absence de l'employé
//        for (Absence absence : absences) {
//            // Ajouter le nombre d'heures travaillées pour chaque absence
//            totalHoursWorked += calculateWorkedHours(absence);
//        }
//
//        return totalHoursWorked;
//    }
//
//    // Méthode pour calculer le nombre d'heures travaillées pour une absence donnée
//    private int calculateWorkedHours(Absence absence) {
//        // Récupérer les détails de l'absence
//        Date startDate = absence.getDate();
//        String absenceType = absence.getAbsenceType();
//        // Initialiser la durée de l'absence à 0
//        int hoursWorked = 0;
//
//        // Vérifier le type d'absence et calculer les heures travaillées en conséquence
//        // Par exemple, supposons que les absences de type "Sick leave" ne sont pas considérées comme des heures travaillées
//        if (!absenceType.equalsIgnoreCase("Sick leave")) {
//            // Calculer la durée de l'absence en heures
//            Date endDate = new Date(); // Date de fin hypothétique
//            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
//            long diffInHours = diffInMillies / (60 * 60 * 1000); // Convertir la différence en heures
//            hoursWorked = (int) diffInHours;
//        }
//
//        return hoursWorked;
//    }
//
//
//
//    // Méthode pour calculer le nombre de fois où l'employé a travaillé sur site
//    public int calculateTimesWorkedOnSite(Employee employee) {
//        // Récupérer le nombre d'absences de l'employé où le type de travail est "site"
//        return absenceRepository.countByEmployeeAndWorkType(employee, "site");
//    }
//
//    // Méthode pour calculer le nombre de fois où les membres du squad ont travaillé sur site
//    public int calculateTimesWorkedOnSiteForSquad(String squadName) {
//        // Récupérer la liste des employés appartenant au squad
//        List<Employee> squadMembers = employeeRepository.findBySquadName(squadName);
//
//        // Initialiser le compteur du nombre de fois où les membres du squad ont travaillé sur site
//        int totalTimesWorkedOnSite = 0;
//
//        // Parcourir chaque membre du squad
//        for (Employee member : squadMembers) {
//            // Ajouter le nombre d'absences où le type de travail est "site" pour chaque membre du squad
//            totalTimesWorkedOnSite += absenceRepository.countByEmployeeAndWorkType(member, "site");
//        }
//
//        return totalTimesWorkedOnSite;
//    }
//
//}