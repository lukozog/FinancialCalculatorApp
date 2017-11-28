package app.financialCalculator.service;

import app.financialCalculator.dao.ExpenseDao;
import app.financialCalculator.dao.UserDao;
import app.financialCalculator.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class UsersExpensesStatisticsService {

    private static final int MAX_DATE = 0;
    private static final int MIN_DATE = 1;
    private static final int FIRST_EXPENSE = 0;
    private static final int NO_EXPENSES = 0;
    private static final int NOT_NULL_DIFFERENTIAL = 1;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private UserExpenseService userExpenseService;

    public Map<Long, Double> usersExpenses(List<Long> listOfUserIds) {
        Map<Long, Double> mapOfUsersExpenses = new TreeMap<>();
        for (Long userId : listOfUserIds) {
            mapOfUsersExpenses.put(userId, userExpenseService.getExpenses(userId));
        }
        return mapOfUsersExpenses;
    }

    public Map<Long, Double> usersExpenses(List<Long> listOfUserIds, long expenseTypeId) {
        Map<Long, Double> mapOfUsersExpenses = new TreeMap<>();
        for (Long userId : listOfUserIds) {
            mapOfUsersExpenses.put(userId, userExpenseService.getExpenses(userId, expenseTypeId));
        }
        return mapOfUsersExpenses;
    }

    public Map<Long, Double> usersExpenses(List<Long> listOfUserIds, LocalDate minDate, LocalDate maxDate) {
        Map<Long, Double> mapOfUsersExpenses = new TreeMap<>();
        for (Long userId : listOfUserIds) {
            mapOfUsersExpenses.put(userId, userExpenseService.getExpenses(userId, minDate, maxDate));
        }
        return mapOfUsersExpenses;
    }

    public Map<Long, Double> usersAverageDailyExpenses(List<Long> listOfUserIds) {
        Map<Long, Double> mapOfUsersExpenses = new TreeMap<>();
        for (Long userId : listOfUserIds) {
            long daysBetween = daysBetween(userId);
            if (daysBetween == 0)
                mapOfUsersExpenses.put(userId, 0.00);
            else
                mapOfUsersExpenses.put(userId, userExpenseService.getExpenses(userId) / daysBetween);
        }
        return mapOfUsersExpenses;
    }

    public Map<Long, Double> usersAverageMonthlyExpenses(List<Long> listOfUserIds) {
        Map<Long, Double> mapOfUsersExpenses = new TreeMap<>();
        for (Long userId : listOfUserIds) {
            long months = monthsBetween(userId);
            if (months == 0)
                mapOfUsersExpenses.put(userId, 0.00);
            else
                mapOfUsersExpenses.put(userId, userExpenseService.getExpenses(userId) / months);
        }
        return mapOfUsersExpenses;
    }

    private long daysBetween(long userId) {
        List<LocalDate> listOfDates = getExtremeDateBounds(userId);
        if (listOfDates == null)
            return NO_EXPENSES;
        if (DAYS.between(listOfDates.get(MIN_DATE), listOfDates.get(MAX_DATE)) == 0) {
            return NOT_NULL_DIFFERENTIAL;
        }
        return DAYS.between(listOfDates.get(MIN_DATE), listOfDates.get(MAX_DATE));
    }

    private long monthsBetween(long userId) {
        List<LocalDate> listOfDates = getExtremeDateBounds(userId);
        if (listOfDates == null)
            return NO_EXPENSES;
        if (listOfDates.get(MAX_DATE).getMonthValue() - listOfDates.get(MIN_DATE).getMonthValue() == 0)
            return NOT_NULL_DIFFERENTIAL;
        return MONTHS.between(listOfDates.get(MIN_DATE).withDayOfMonth(1), listOfDates.get(MAX_DATE).withDayOfMonth(1));
    }

    private List<LocalDate> getExtremeDateBounds(long userId) {
        List<Expense> userExpenses = expenseDao.getExpenses(userId);
        if (userExpenses.isEmpty())
            return null;
        LocalDate minDate = expenseDao.getExpenses(userId).get(FIRST_EXPENSE).getExpenseDate();
        LocalDate maxDate = minDate;
        for (Expense e : userExpenses) {
            if (e.getExpenseDate().isAfter(maxDate)) {
                maxDate = e.getExpenseDate();
            }
            if (e.getExpenseDate().isBefore(minDate)) {
                minDate = e.getExpenseDate();
            }
        }
        List<LocalDate> listOfDates = new ArrayList<>();
        listOfDates.add(maxDate);
        listOfDates.add(minDate);
        return listOfDates;
    }
}
