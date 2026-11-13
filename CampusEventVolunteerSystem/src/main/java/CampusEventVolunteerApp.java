import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CampusEventVolunteerApp {

    static JFrame frame;

    // BLUE & WHITE THEME
    private static final Color PRIMARY = new Color(35, 105, 200);
    private static final Color NAVY = new Color(20, 42, 78);
    private static final Color PAGE_BG = new Color(239, 245, 253);
    private static final Color BORDER = new Color(195, 211, 232);

    public static void main(String[] args) {
        applyTheme();
        SwingUtilities.invokeLater(CampusEventVolunteerApp::showLogin);
    }

    static void applyTheme() {
        UIManager.put("Panel.background", PAGE_BG);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 13));
        UIManager.put("Button.focus", new Color(180, 205, 240));
        UIManager.put("TableHeader.background", NAVY);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("Table.selectionBackground", new Color(205, 225, 250));
        UIManager.put("Table.selectionForeground", NAVY);
        UIManager.put("TextField.selectionBackground", new Color(205, 225, 250));
    }

    static void styleButton(JButton button) {
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
    }

    static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(205, 225, 250));
        table.setSelectionForeground(NAVY);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    static void showLogin() {

        frame = new JFrame("Campus Event Volunteer Management System");
        frame.setSize(520, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(235, 243, 255));

        JLabel title = new JLabel(
                "<html><center>Campus Event Volunteer<br>" +
                        "Management System</center></html>",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(20, 48, 92));
        title.setBorder(BorderFactory.createEmptyBorder(35, 10, 25, 10));

        JPanel form = new JPanel(new GridLayout(5, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 70, 35, 70));
        form.setBackground(Color.WHITE);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        username.setBorder(BorderFactory.createTitledBorder("Username"));
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton login = new JButton("LOGIN");
        styleButton(login);

        JLabel info = new JLabel(
                "Login using registered database credentials",
                SwingConstants.CENTER
        );

        JLabel college = new JLabel(
                "Poornima Institute of Engineering and Technology, Jaipur",
                SwingConstants.CENTER
        );

        form.add(username);
        form.add(password);
        form.add(login);
        form.add(info);
        form.add(college);

        login.addActionListener(e -> {

            String usernameText = username.getText().trim();
            String passwordText = new String(password.getPassword());

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter Username and Password.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, usernameText);
                pst.setString(2, passwordText);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.dispose();
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid Username or Password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Database Error:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);

        frame.add(main);
        frame.setVisible(true);
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    static void showDashboard() {

        frame = new JFrame("Campus Event Volunteer Management System - Dashboard");
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidebar = createSidebar();

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(PAGE_BG);

        JLabel heading = new JLabel("Admin Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(NAVY);
        heading.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 20));

        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        cards.setBackground(PAGE_BG);

        cards.add(createCard("Total Events", getCount("events")));
        cards.add(createCard("Total Volunteers", getCount("volunteers")));
        cards.add(createCard("Total Assignments", getCount("assignments")));
        cards.add(createCard("System Status", "ACTIVE"));

        content.add(heading, BorderLayout.NORTH);
        content.add(cards, BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(content, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    static JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel(
                "<html><center>Campus Event<br>Volunteer System</center></html>",
                SwingConstants.CENTER
        );

        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        sidebar.add(logo);

        JButton dashboard = sideButton("Dashboard");
        JButton events = sideButton("Event Management");
        JButton volunteers = sideButton("Volunteer Management");
        JButton assignments = sideButton("Assignments");
        JButton logout = sideButton("Logout");

        dashboard.addActionListener(e -> {
            frame.dispose();
            showDashboard();
        });

        events.addActionListener(e -> {
            frame.dispose();
            showEvents();
        });

        volunteers.addActionListener(e -> {
            frame.dispose();
            showVolunteers();
        });

        assignments.addActionListener(e -> {
            frame.dispose();
            showAssignments();
        });

        logout.addActionListener(e -> {
            frame.dispose();
            showLogin();
        });

        sidebar.add(dashboard);
        sidebar.add(events);
        sidebar.add(volunteers);
        sidebar.add(assignments);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logout);

        return sidebar;
    }

    static JButton sideButton(String text) {

        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(205, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(40, 75, 125));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setOpaque(true);

        return button;
    }

    // =========================================================
    // DASHBOARD CARDS
    // =========================================================

    static JPanel createCard(String title, String value) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        t.setForeground(NAVY);

        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Arial", Font.BOLD, 32));
        v.setForeground(PRIMARY);

        panel.add(t, BorderLayout.NORTH);
        panel.add(v, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // EVENT MANAGEMENT
    // =========================================================

    static void showEvents() {

        frame = new JFrame("Event Management");
        frame.setSize(1150, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidebar = createSidebar();

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Event Management");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(NAVY);
        title.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));
        form.setBackground(Color.WHITE);

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField date = new JTextField();
        JTextField venue = new JTextField();
        JTextField description = new JTextField();

        form.add(new JLabel("Event ID"));
        form.add(id);
        form.add(new JLabel("Event Name"));
        form.add(name);
        form.add(new JLabel("Date (YYYY-MM-DD)"));
        form.add(date);
        form.add(new JLabel("Venue"));
        form.add(venue);
        form.add(new JLabel("Description"));
        form.add(description);

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);

        JButton add = new JButton("ADD");
        JButton update = new JButton("UPDATE");
        JButton delete = new JButton("DELETE");
        JButton refresh = new JButton("REFRESH");

        styleButton(add);
        styleButton(update);
        styleButton(delete);
        styleButton(refresh);

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(refresh);

        String[] columns = {
                "Event ID", "Event Name", "Date", "Venue", "Description"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);

        loadEvents(model);

        add.addActionListener(e -> {

            String sql = "INSERT INTO events " +
                    "(event_id,event_name,event_date,venue,description) VALUES (?,?,?,?,?)";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, id.getText());
                pst.setString(2, name.getText());
                pst.setDate(3, Date.valueOf(date.getText()));
                pst.setString(4, venue.getText());
                pst.setString(5, description.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Event Added Successfully!");

                loadEvents(model);
                clearFields(id, name, date, venue, description);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        update.addActionListener(e -> {

            String sql = "UPDATE events SET event_name=?, event_date=?, venue=?, description=? WHERE event_id=?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, name.getText());
                pst.setDate(2, Date.valueOf(date.getText()));
                pst.setString(3, venue.getText());
                pst.setString(4, description.getText());
                pst.setString(5, id.getText());

                int rows = pst.executeUpdate();

                JOptionPane.showMessageDialog(frame,
                        rows > 0 ? "Event Updated Successfully!" : "Event ID not found.");

                loadEvents(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        delete.addActionListener(e -> {

            String sql = "DELETE FROM events WHERE event_id=?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, id.getText());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Event Deleted Successfully!");
                loadEvents(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        refresh.addActionListener(e -> loadEvents(model));

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {
                id.setText(table.getValueAt(row, 0).toString());
                name.setText(table.getValueAt(row, 1).toString());
                date.setText(table.getValueAt(row, 2).toString());
                venue.setText(table.getValueAt(row, 3).toString());
                description.setText(table.getValueAt(row, 4).toString());
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(main, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // =========================================================
    // LOAD EVENTS
    // =========================================================

    static void loadEvents(DefaultTableModel model) {

        model.setRowCount(0);

        String sql = "SELECT * FROM events";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("event_id"),
                        rs.getString("event_name"),
                        rs.getDate("event_date"),
                        rs.getString("venue"),
                        rs.getString("description")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // =========================================================
    // VOLUNTEER MANAGEMENT
    // =========================================================

    static void showVolunteers() {

        frame = new JFrame("Volunteer Management");
        frame.setSize(1150, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidebar = createSidebar();

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Volunteer Management");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(NAVY);
        title.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));
        form.setBackground(Color.WHITE);

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField department = new JTextField();

        form.add(new JLabel("Volunteer ID"));
        form.add(id);
        form.add(new JLabel("Name"));
        form.add(name);
        form.add(new JLabel("Email"));
        form.add(email);
        form.add(new JLabel("Phone"));
        form.add(phone);
        form.add(new JLabel("Department"));
        form.add(department);

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);

        JButton add = new JButton("ADD");
        JButton delete = new JButton("DELETE");
        JButton refresh = new JButton("REFRESH");

        styleButton(add);
        styleButton(delete);
        styleButton(refresh);

        buttons.add(add);
        buttons.add(delete);
        buttons.add(refresh);

        String[] columns = {
                "Volunteer ID", "Name", "Email", "Phone", "Department"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);

        loadVolunteers(model);

        add.addActionListener(e -> {

            String sql = "INSERT INTO volunteers " +
                    "(volunteer_id,name,email,phone,department) VALUES (?,?,?,?,?)";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, id.getText());
                pst.setString(2, name.getText());
                pst.setString(3, email.getText());
                pst.setString(4, phone.getText());
                pst.setString(5, department.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Volunteer Added Successfully!");

                loadVolunteers(model);
                clearFields(id, name, email, phone, department);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        delete.addActionListener(e -> {

            String sql = "DELETE FROM volunteers WHERE volunteer_id=?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, id.getText());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Volunteer Deleted Successfully!");
                loadVolunteers(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        refresh.addActionListener(e -> loadVolunteers(model));

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {
                id.setText(table.getValueAt(row, 0).toString());
                name.setText(table.getValueAt(row, 1).toString());
                email.setText(table.getValueAt(row, 2).toString());
                phone.setText(table.getValueAt(row, 3).toString());
                department.setText(table.getValueAt(row, 4).toString());
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(main, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // =========================================================
    // LOAD VOLUNTEERS
    // =========================================================

    static void loadVolunteers(DefaultTableModel model) {

        model.setRowCount(0);

        String sql = "SELECT * FROM volunteers";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("volunteer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // =========================================================
    // ASSIGNMENTS
    // =========================================================

    static void showAssignments() {

        frame = new JFrame("Event Volunteer Assignment");
        frame.setSize(1100, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidebar = createSidebar();

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Event-Volunteer Assignment");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(NAVY);
        title.setBorder(BorderFactory.createEmptyBorder(25, 25, 20, 25));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        form.setBackground(Color.WHITE);

        JTextField assignmentID = new JTextField();
        JTextField eventID = new JTextField();
        JTextField volunteerID = new JTextField();
        JTextField role = new JTextField();

        form.add(new JLabel("Assignment ID"));
        form.add(assignmentID);
        form.add(new JLabel("Event ID"));
        form.add(eventID);
        form.add(new JLabel("Volunteer ID"));
        form.add(volunteerID);
        form.add(new JLabel("Role"));
        form.add(role);

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);

        JButton assign = new JButton("ASSIGN");
        JButton delete = new JButton("DELETE");
        JButton refresh = new JButton("REFRESH");

        styleButton(assign);
        styleButton(delete);
        styleButton(refresh);

        buttons.add(assign);
        buttons.add(delete);
        buttons.add(refresh);

        String[] columns = {
                "Assignment ID", "Event ID", "Volunteer ID", "Role"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);

        loadAssignments(model);

        assign.addActionListener(e -> {

            String sql = "INSERT INTO assignments " +
                    "(assignment_id,event_id,volunteer_id,role) VALUES (?,?,?,?)";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, assignmentID.getText());
                pst.setString(2, eventID.getText());
                pst.setString(3, volunteerID.getText());
                pst.setString(4, role.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Volunteer Assigned Successfully!");

                loadAssignments(model);
                clearFields(assignmentID, eventID, volunteerID, role);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        delete.addActionListener(e -> {

            String sql = "DELETE FROM assignments WHERE assignment_id=?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, assignmentID.getText());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Assignment Deleted Successfully!");
                loadAssignments(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        refresh.addActionListener(e -> loadAssignments(model));

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {
                assignmentID.setText(table.getValueAt(row, 0).toString());
                eventID.setText(table.getValueAt(row, 1).toString());
                volunteerID.setText(table.getValueAt(row, 2).toString());
                role.setText(table.getValueAt(row, 3).toString());
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(main, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // =========================================================
    // LOAD ASSIGNMENTS
    // =========================================================

    static void loadAssignments(DefaultTableModel model) {

        model.setRowCount(0);

        String sql = "SELECT * FROM assignments";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("assignment_id"),
                        rs.getString("event_id"),
                        rs.getString("volunteer_id"),
                        rs.getString("role")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // =========================================================
    // DATABASE COUNT
    // =========================================================

    static String getCount(String table) {

        String sql = "SELECT COUNT(*) FROM " + table;

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }

        } catch (SQLException ex) {
            return "0";
        }

        return "0";
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    static void clearFields(JTextField... fields) {

        for (JTextField field : fields) {
            field.setText("");
        }
    }
}