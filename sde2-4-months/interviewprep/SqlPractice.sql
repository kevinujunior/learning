--1. Find the second highest salary in a table.
-- Employee(id, name, salary)

--a
select
    Max(salary)
from
    Employee
where
    salary < (
        select
            max(salary)
        from
            Employee
    ) 
--b
select
    salary
from
    Employee
order by
    salary desc
limit
    1 offset 1;

--c 
select
    salary
from
    (
        select
            *,
            dense_rank() over (
                order by
                    salary desc
            ) as rank
    )
from
    Employee
where
    rank = 2;


--2. Find duplicate records in a table.
--Users(id, email)
select
    email,
    count(*)
from
    Users
group by
    email
having
    count(*) > 1;


--3. Delete duplicate rows but keep one record.
delete from
    Users
where
    id not in (
        select
            min(id)
        from
            users
        group by
            email
    ) 
    
--4. Find employees earning more than their manager.
--Employee(id, name, salary, manager_id)
select
    e1.name
from
    employee e1,
    employee e2
where
    e1.salary > e2.salary
    and e1.manager_id = e2.id;

--5. Get the Nth highest salary.
select
    salary
from
    Employee
order by
    salary desc
limit
    1 offset N -1;


--6.Find customers who never placed an order.
--Customers(id, name, email)
--Orders(id, customer_id, order_date, amount)
SELECT
    c.name
FROM
    Customers c
    LEFT JOIN Orders o ON c.id = o.customer_id
WHERE
    o.id IS NULL;


--7. Find the highest salary in each department.
-- Employee(id, name, salary, department_id)
--a
select
    max(salary) as max_dep_salry,
    department_id
from
    employee
group by
    department_id;

--b
select
    *
from
    (
        select
            *,
            dense_rank() over (
                partition by department_id
                order by
                    salary desc
            ) as rank
        from
            Employee
    )
where
    rank = 1;

--8. Find consecutive duplicate values.
--Logs(id, num)
select
    distinct num
from
    Logs l1
    join logs l2 on l1.id = l2.id -1
where
    l1.num = l2.num 
    
--9. Find top 3 salaries in each department.
select
    *
from
    (
        select
            *,
            dense_rank() over (
                partition by department_id
                order by
                    salary desc
            ) as rank
        from
            Employee
    )
where
    rank <= 3
order by
    department_id,
    rank;

--10. Find employees hired in the last 30 days.
SELECT
    *
FROM
    Employee
WHERE
    hire_date >= CURRENT_DATE - INTERVAL 30 DAY;