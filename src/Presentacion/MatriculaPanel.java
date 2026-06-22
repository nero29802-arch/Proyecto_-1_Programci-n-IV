package Presentacion;

import Excepciones.DatoInvalidoException;
import Excepciones.RegistroDuplicadoException;
import Modelo.Curso;
import Modelo.Estudiante;
import Modelo.Matricula;
import Negocio.CursoNegocio;
import Negocio.EstudianteNegocio;
import Negocio.MatriculaNegocio;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MatriculaPanel extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MatriculaNegocio negocio;
    private final EstudianteNegocio estudianteNegocio;
    private final CursoNegocio cursoNegocio;

    private JComboBox<Estudiante> cboEstudiante;
    private JComboBox<Curso> cboCurso;
    private JTextField txtFecha;
    private JButton btnMatricular, btnLimpiar, btnEditar, btnAnular, btnEliminar, btnActualizarListas;

    private JTextField txtBuscar;
    private JComboBox<String> cboBuscarEn, cboOrdenar;
    private JButton btnBuscar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private int idSeleccionado = -1;

    public MatriculaPanel(EstudianteNegocio estudianteNegocio, CursoNegocio cursoNegocio, MatriculaNegocio negocio) {
        this.estudianteNegocio = estudianteNegocio;
        this.cursoNegocio = cursoNegocio;
        this.negocio = negocio;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        refrescarListas();
        limpiar();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboEstudiante = new JComboBox<>();
        cboCurso = new JComboBox<>();
        txtFecha = new JTextField(LocalDate.now().format(FORMATO_FECHA), 10);

        agregarCampo(panel, gbc, "Estudiante:", cboEstudiante, 0, 0);
        agregarCampo(panel, gbc, "Curso:", cboCurso, 2, 0);
        agregarCampo(panel, gbc, "Fecha (dd/mm/aaaa):", txtFecha, 0, 1);

        btnMatricular = new JButton("Matricular");
        btnLimpiar = new JButton("Limpiar");
        btnEditar = new JButton("Editar fecha");
        btnAnular = new JButton("Anular");
        btnEliminar = new JButton("Eliminar");
        btnActualizarListas = new JButton("Actualizar listas");

        btnEditar.setEnabled(false);
        btnAnular.setEnabled(false);
        btnEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnMatricular);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnAnular);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizarListas);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6;
        panel.add(panelBotones, gbc);

        txtBuscar = new JTextField(15);
        cboBuscarEn = new JComboBox<>(new String[]{"Todos", "Estudiante", "Curso", "Estado"});
        btnBuscar = new JButton("Buscar");
        cboOrdenar = new JComboBox<>(new String[]{"Sin ordenar", "Por fecha", "Por estudiante"});

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(new JLabel("En:"));
        panelBusqueda.add(cboBuscarEn);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(new JLabel("Ordenar:"));
        panelBusqueda.add(cboOrdenar);

        gbc.gridy = 3;
        panel.add(panelBusqueda, gbc);

        btnMatricular.addActionListener(e -> guardarMatricula());
        btnLimpiar.addActionListener(e -> limpiar());
        btnEditar.addActionListener(e -> editarMatricula());
        btnAnular.addActionListener(e -> anularOReactivarMatricula());
        btnEliminar.addActionListener(e -> eliminarMatricula());
        btnActualizarListas.addActionListener(e -> refrescarListas());
        btnBuscar.addActionListener(e -> buscarMatricula());
        cboOrdenar.addActionListener(e -> ordenarMatriculas());

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent campo, int col, int fila) {
        gbc.gridwidth = 1;
        gbc.gridx = col; gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = col + 1;
        panel.add(campo, gbc);
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Estudiante", "Curso", "Créditos", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarMatriculaSeleccionada();
            }
        });
        return new JScrollPane(tabla);
    }

    private void guardarMatricula() {
        try {
            Estudiante estudiante = (Estudiante) cboEstudiante.getSelectedItem();
            Curso curso = (Curso) cboCurso.getSelectedItem();
            LocalDate fecha = parsearFecha();

            negocio.matricular(estudiante, curso, fecha);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Matrícula registrada correctamente.");
        } catch (DatoInvalidoException | RegistroDuplicadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Solo se edita la fecha; estudiante/curso quedan fijos (si se equivocaron, anulan y crean otra). */
    private void editarMatricula() {
        if (idSeleccionado == -1) return;
        try {
            Matricula original = negocio.buscarPorId(idSeleccionado);
            if (original == null) return;

            LocalDate fecha = parsearFecha();
            Matricula editada = new Matricula(
                    idSeleccionado, original.getEstudiante(), original.getCurso(), fecha, original.getEstado());

            negocio.editar(editada);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Fecha de matrícula actualizada correctamente.");
        } catch (DatoInvalidoException | RegistroDuplicadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Un mismo botón Anula (si está Activa) o Reactiva (si está Anulada). */
    private void anularOReactivarMatricula() {
        if (idSeleccionado == -1) return;
        Matricula m = negocio.buscarPorId(idSeleccionado);
        if (m == null) return;

        if (m.isActiva()) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea anular esta matrícula?", "Confirmar anulación",
                    JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                negocio.anular(idSeleccionado);
                actualizarTabla(negocio.obtenerTodos());
                limpiar();
                JOptionPane.showMessageDialog(this, "Matrícula anulada correctamente.");
            }
        } else {
            try {
                negocio.reactivar(idSeleccionado);
                actualizarTabla(negocio.obtenerTodos());
                limpiar();
                JOptionPane.showMessageDialog(this, "Matrícula reactivada correctamente.");
            } catch (DatoInvalidoException | RegistroDuplicadoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarMatricula() {
        if (idSeleccionado == -1) return;
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar esta matrícula? Esta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            negocio.eliminar(idSeleccionado);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Matrícula eliminada correctamente.");
        }
    }

    private void buscarMatricula() {
        String texto = txtBuscar.getText().trim();
        String campo = (String) cboBuscarEn.getSelectedItem();
        if (texto.isEmpty()) {
            actualizarTabla(negocio.obtenerTodos());
        } else {
            actualizarTabla(negocio.buscar(texto, campo));
        }
    }

    private void ordenarMatriculas() {
        String opcion = (String) cboOrdenar.getSelectedItem();
        switch (opcion) {
            case "Por fecha": actualizarTabla(negocio.ordenarPorFecha()); break;
            case "Por estudiante": actualizarTabla(negocio.ordenarPorEstudiante()); break;
            default: actualizarTabla(negocio.obtenerTodos());
        }
    }

    private void cargarMatriculaSeleccionada() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        Matricula m = negocio.buscarPorId(idSeleccionado);
        if (m == null) return;

        cboEstudiante.setSelectedItem(m.getEstudiante());
        cboCurso.setSelectedItem(m.getCurso());
        txtFecha.setText(m.getFechaMatricula().format(FORMATO_FECHA));

        cboEstudiante.setEnabled(false);
        cboCurso.setEnabled(false);

        btnMatricular.setEnabled(false);
        btnEditar.setEnabled(true);
        btnAnular.setEnabled(true);
        btnAnular.setText(m.isActiva() ? "Anular" : "Reactivar");
        btnEliminar.setEnabled(true);
    }

    private LocalDate parsearFecha() throws DatoInvalidoException {
        String texto = txtFecha.getText().trim();
        try {
            return LocalDate.parse(texto, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("La fecha debe tener el formato dd/mm/aaaa.");
        }
    }

    private void actualizarTabla(List<Matricula> lista) {
        modeloTabla.setRowCount(0);
        for (Matricula m : lista) {
            modeloTabla.addRow(new Object[]{
                m.getId(),
                m.getEstudiante().getNombre(),
                m.getCurso().getCodigo() + " - " + m.getCurso().getNombre(),
                m.getCurso().getCreditos(),
                m.getFechaMatricula().format(FORMATO_FECHA),
                m.getEstado()
            });
        }
    }

    /** Recarga los combos con estudiantes/cursos ACTIVOS y refresca la tabla. */
    public final void refrescarListas() {
        Estudiante estudianteSeleccionado = (Estudiante) cboEstudiante.getSelectedItem();
        Curso cursoSeleccionado = (Curso) cboCurso.getSelectedItem();

        cboEstudiante.removeAllItems();
        for (Estudiante e : estudianteNegocio.obtenerTodos()) {
            if (e.isActivo()) cboEstudiante.addItem(e);
        }
        if (estudianteSeleccionado != null) cboEstudiante.setSelectedItem(estudianteSeleccionado);

        cboCurso.removeAllItems();
        for (Curso c : cursoNegocio.obtenerActivos()) {
            cboCurso.addItem(c);
        }
        if (cursoSeleccionado != null) cboCurso.setSelectedItem(cursoSeleccionado);

        actualizarTabla(negocio.obtenerTodos());
    }

    private void limpiar() {
        if (cboEstudiante.getItemCount() > 0) cboEstudiante.setSelectedIndex(0);
        if (cboCurso.getItemCount() > 0) cboCurso.setSelectedIndex(0);
        cboEstudiante.setEnabled(true);
        cboCurso.setEnabled(true);
        txtFecha.setText(LocalDate.now().format(FORMATO_FECHA));

        idSeleccionado = -1;
        tabla.clearSelection();

        btnMatricular.setEnabled(true);
        btnEditar.setEnabled(false);
        btnAnular.setEnabled(false);
        btnAnular.setText("Anular");
        btnEliminar.setEnabled(false);
    }

    public MatriculaNegocio getNegocio() {
        return negocio;
    }
}
