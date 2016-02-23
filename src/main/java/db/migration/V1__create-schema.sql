CREATE table `projects` (
  `id` int(11) AUTO_INCREMENT PRIMARY key,
  `name` varchar(150) not null,
  `customer` varchar(255) not null,
  `executor` varchar(255) null,
  `begin_date` timestamp not null,
  `end_date` TIMESTAMP null
);

create table `employee` (
  `id` int(11) AUTO_INCREMENT primary key,
  `first_name` varchar(50) not null,
  `last_name` varchar(100) not null
);

create table `project_employee` (
  `project_id` int(11) not null,
  `employee_id` int(11) not null,
  PRIMARY KEY (`project_id`, `employee_id`),
  FOREIGN KEY (`project_id`) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`employee_id`) REFERENCES employee(id) ON UPDATE CASCADE ON DELETE CASCADE
)