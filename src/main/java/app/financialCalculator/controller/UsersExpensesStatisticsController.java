package app.financialCalculator.controller;

import app.financialCalculator.service.UsersExpensesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("usersExpensesStatistics")
public class UsersExpensesStatisticsController {

    @Autowired
    private UsersExpensesStatisticsService usersExpensesStatisticsService;

    @PostMapping("/usersExpenses")
    public Map<Long, Double> usersExpenses(@RequestBody List<Long> listOfUsersId) {
        return usersExpensesStatisticsService.usersExpenses(listOfUsersId);
    }

    @PostMapping("usersExpenses/{expenseTypeId}")
    public Map<Long, Double> usersExpenses(@RequestBody List<Long> listOfUsersId, @PathVariable long expenseTypeId) {
        return usersExpensesStatisticsService.usersExpenses(listOfUsersId, expenseTypeId);
    }

    @PostMapping("usersExpenses/{minDate}/{maxDate}")
    public Map<Long, Double> usersExpenses(@RequestBody List<Long> listOfUsersId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate minDate, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate maxDate) {
        return usersExpensesStatisticsService.usersExpenses(listOfUsersId, minDate, maxDate);
    }

    @PostMapping("/usersAverageDailyExpenses")
    public Map<Long, Double> usersAverageDailyExpenses(@RequestBody List<Long> listOfUsersId) {
        return usersExpensesStatisticsService.usersAverageDailyExpenses(listOfUsersId);
    }

    @PostMapping("/usersAverageMonthlyExpenses")
    public Map<Long, Double> usersAverageMonthlyExpenses(@RequestBody List<Long> listOfUsersId) {
        return usersExpensesStatisticsService.usersAverageMonthlyExpenses(listOfUsersId);
    }
}
