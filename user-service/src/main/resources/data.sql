DELETE FROM users;

insert into users(id, email, username, password, first_name, last_name, address, account_number, role) VALUES
(1, 'admin@gmail.com', 'admin', '21232F297A57A5A743894A0E4A801FC3', 'Adnan', 'Ataya', '777 Brockton Avenue, Abington MA 2351', '12345678', 'ADMIN'),
(2, 'adnan@gmail.com', 'adnan', 'D1A0A9E9391AF09E978C4C3D11711E75', 'Adnan', 'Adnan', '30 Memorial Drive, Avon MA 2322', '88888888', 'USER'),
(3, 'ataya@gmail.com', 'ataya', '03EDB16EA044EDF5F8CB502AD9BA8965', 'Ataya', 'Ataya', null, '0453259', 'USER'),
(4, 'david@gmail.com', 'david', '172522EC1028AB781D9DFD17EACA4427', 'David', 'Morrison', '55 Brooksby Village Way, Danvers MA 1923', '0883252', 'USER'),
(5, 'john@gmail.com', 'john', '527BD5B5D689E2C32AE974C6229FF785', 'John', 'Doe', '137 Teaticket Hwy, East Falmouth MA 2536', '369170630248', 'USER'),
(6, 'kevin@gmail.com', 'kevin', '9D5E3ECDEB4CDB7ACFD63075AE046672', 'Kevin', 'Ryan', '200 Otis Street, Northborough MA 1532', null, 'USER'),
(7, 'derek@gmail.com', 'derek', '769F2B8A75180C1E8C9B37CCBCF9E049', 'Derek', 'Powell', '1180 Fall River Avenue, Seekonk MA 2771', '729056', 'USER'),
(8, 'miriam@gmail.com', 'miriam', '277F8D77FCEB01B44ADD2092FF8BBC33', 'Miriam', 'Russell', '139 Merchant Place, Cobleskill NY 12043', '2824026344016351', 'USER'),
(9, 'william@gmail.com', 'william', 'FD820A2B4461BDDD116C1518BC4B0F77', 'William', 'Ryan', '41 Anawana Lake Road, Monticello NY 12701', '56423678653', 'USER'),
(10, 'product@gmail.com', 'product', 'F5BF48AA40CAD7891EB709FCF1FDE128', 'Product', 'Manager', null, null, 'PRODUCT_MANAGER'),
(11, 'marketing@gmail.com', 'marketing', 'C769C2BD15500DD906102D9BE97FDCEB', 'Marketing', 'Manager', null, null, 'MARKETING_MANAGER'),
(12, 'sales@gmail.com', 'sales', '9ED083B1436E5F40EF984B28255EEF18', 'Sales', 'Manager', null, null, 'SALES_MANAGER')
;
