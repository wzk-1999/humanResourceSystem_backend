create database BO-hr-system;

CREATE OR REPLACE FUNCTION create_user_credentials() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_credentials (username, password, initial_password, is_initial_password,tb_staff_id,role)
    VALUES (NEW.tb_staff_id::TEXT, NEW.tb_staff_id::TEXT || NEW.name, NEW.tb_staff_id::TEXT || NEW.name, true,NEW.tb_staff_id::text,'Staff');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_staff_insert
AFTER INSERT ON bo_staffs_info
FOR EACH ROW
EXECUTE FUNCTION create_user_credentials();

-- Create the trigger function to delete user_credentials
CREATE OR REPLACE FUNCTION delete_user_credentials() RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM user_credentials WHERE tb_staff_id = OLD.tb_staff_id::TEXT;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger to call the function after delete
CREATE TRIGGER after_staff_delete
AFTER DELETE ON bo_staffs_info
FOR EACH ROW
EXECUTE FUNCTION delete_user_credentials();

INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Supplier', 'clockin,home', 'header');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('SystemAdmin', 'permissionManagement', 'header');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Admin', 'profile', 'dropdown');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Admin', 'clockin,home,salary,telentDiscover,supplierManagement', 'header');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Staff', 'profile', 'dropdown');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Staff', 'clockin,home,salary', 'header');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Supplier-Manager', 'profile', 'dropdown');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('Supplier-Manager', 'clockin,home,salary,supplierManagement', 'header');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('HR', 'profile', 'dropdown');
INSERT INTO public.role_permission_manus
(role_permission_role, manus, description)
VALUES('HR', 'clockin,home,salary,telentDiscover', 'header');