/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

/**
 *
 * @author HP
 */
import Excepciones.DatoInvalidoException;
import Excepciones.RegistroDuplicadoException;
import Modelo.Curso;
import Negocio.CursoNegocio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Capa de Presentación del módulo Cursos.
 * Esta clase SOLO captura datos del formulario, llama a CursoNegocio,
 * y actualiza la tabla/mensajes. No contiene ninguna validación de negocio:
 * todas las reglas (código obligatorio, créditos > 0, duplicados, etc.)
 * viven en CursoNegocio y llegan aquí como excepciones.
 */
public class CursoPanel extends JPanel {

    private CursoNegocio negocio;

    // Componentes del formulario
    private JTextField txtCodigo, txtNombre, txtCreditos, txtProfesor;
    private JCheckBox chkActivo;
    private JButton btnGuardar, btnLimpiar, btnEditar, btnEliminar;

    // Componentes de búsqueda y ordenamiento
    private JTextField txtBuscar;
    private JComboBox<String> cboBuscarEn, cboOrdenar;
    private JButton btnBuscar;

    // Tabla donde se listan los cursos
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Guarda el ID del curso actualmente seleccionado en la tabla (-1 = ninguno)
    private int idSeleccionado = -1;

    public CursoPanel(CursoNegocio negocio) {
        this.negocio = negocio;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        actualizarTabla(negocio.obtenerTodos());
    }

    /**
     * Construye el formulario superior: campos de texto, checkbox,
     * botones de acción y la barra de búsqueda/ordenamiento.
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Curso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo   = new JTextField(10);
        txtNombre   = new JTextField(20);
        txtCreditos = new JTextField(5);
        txtProfesor = new JTextField(20);
        chkActivo   = new JCheckBox("Activo", true);

        agregarCampo(panel, gbc, "Código:",    txtCodigo,   0, 0);
        agregarCampo(panel, gbc, "Nombre:",    txtNombre,   0, 1);
        agregarCampo(panel, gbc, "Créditos:",  txtCreditos, 2, 0);
        agregarCampo(panel, gbc, "Profesor:",  txtProfesor, 2, 1);

        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(chkActivo, gbc);

        // Botones principales de acción
        btnGuardar  = new JButton("Guardar");
        btnLimpiar  = new JButton("Limpiar");
        btnEditar   = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        // Editar y Eliminar inician deshabilitados hasta que se seleccione una fila
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6;
        panel.add(panelBotones, gbc);

        // Barra de búsqueda y ordenamiento
        txtBuscar    = new JTextField(15);
        cboBuscarEn  = new JComboBox<>(new String[]{"Todos", "Código", "Nombre"});
        btnBuscar    = new JButton("Buscar");
        cboOrdenar   = new JComboBox<>(new String[]{"Sin ordenar", "Por código", "Por nombre"});

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

        // Conexión de eventos: cada botón llama a su propio método privado
        btnGuardar.addActionListener(e -> guardarCurso());
        btnLimpiar.addActionListener(e -> limpiar());
        btnEditar.addActionListener(e -> editarCurso());
        btnEliminar.addActionListener(e -> eliminarCurso());
        btnBuscar.addActionListener(e -> buscarCurso());
        cboOrdenar.addActionListener(e -> ordenarCursos());

        return panel;
    }

    /**
     * Método auxiliar para no repetir código: agrega una etiqueta + su campo
     * en una posición específica del GridBagLayout.
     */
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent campo, int col, int fila) {
        gbc.gridwidth = 1;
        gbc.gridx = col; gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = col + 1;
        panel.add(campo, gbc);
    }

    /**
     * Construye la tabla (JTable) donde se listan todos los cursos registrados.
     * Al seleccionar una fila, se cargan sus datos en el formulario para editar.
     */
    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Código", "Nombre", "Créditos", "Profesor", "Activo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; } // tabla de solo lectura
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarCursoSeleccionado();
            }
        });
        return new JScrollPane(tabla);
    }

    /**
     * Toma los datos del formulario, los envía a CursoNegocio.agregar()
     * y muestra un mensaje de éxito o de error según corresponda.
     */
    private void guardarCurso() {
        try {
            Curso curso = obtenerDatosFormulario(0); // el ID real lo asigna el Repositorio
            negocio.agregar(curso);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Curso registrado correctamente.");
        } catch (DatoInvalidoException | RegistroDuplicadoException e) {
            // Mensaje claro y específico (no genérico) mostrado al usuario
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Envía los datos editados del curso seleccionado a CursoNegocio.editar().
     */
    private void editarCurso() {
        if (idSeleccionado == -1) return;
        try {
            Curso curso = obtenerDatosFormulario(idSeleccionado);
            negocio.editar(curso);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Curso actualizado correctamente.");
        } catch (DatoInvalidoException | RegistroDuplicadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pide confirmación con JOptionPane antes de eliminar (requisito obligatorio del proyecto).
     */
    private void eliminarCurso() {
        if (idSeleccionado == -1) return;
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este curso?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            negocio.eliminar(idSeleccionado);
            actualizarTabla(negocio.obtenerTodos());
            limpiar();
            JOptionPane.showMessageDialog(this, "Curso eliminado correctamente.");
        }
    }

    /**
     * Busca cursos según el texto y el campo seleccionado (Código, Nombre o Todos).
     * Si el campo de búsqueda está vacío, muestra todos los cursos de nuevo.
     */
    private void buscarCurso() {
        String texto = txtBuscar.getText().trim();
        String campo = (String) cboBuscarEn.getSelectedItem();
        if (texto.isEmpty()) {
            actualizarTabla(negocio.obtenerTodos());
        } else {
            actualizarTabla(negocio.buscar(texto, campo));
        }
    }

    /**
     * Ordena la tabla según la opción elegida en el combo de ordenamiento.
     */
    private void ordenarCursos() {
        String opcion = (String) cboOrdenar.getSelectedItem();
        switch (opcion) {
            case "Por código": actualizarTabla(negocio.ordenarPorCodigo()); break;
            case "Por nombre": actualizarTabla(negocio.ordenarPorNombre()); break;
            default: actualizarTabla(negocio.obtenerTodos());
        }
    }

    /**
     * Cuando el usuario hace clic en una fila de la tabla, carga esos datos
     * en el formulario para poder editarlos o eliminarlos.
     */
    private void cargarCursoSeleccionado() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtCodigo.setText((String) modeloTabla.getValueAt(fila, 1));
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 2));
        txtCreditos.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtProfesor.setText((String) modeloTabla.getValueAt(fila, 4));
        chkActivo.setSelected((boolean) modeloTabla.getValueAt(fila, 5));
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnGuardar.setEnabled(false); // mientras se edita, no se puede "guardar" como nuevo
    }

    /**
     * Lee los datos escritos en el formulario y arma un objeto Curso.
     * Solo valida que "Créditos" sea numérico (error de formato, no de negocio);
     * el resto de validaciones de negocio las hace CursoNegocio.
     */
    private Curso obtenerDatosFormulario(int id) throws DatoInvalidoException {
        String codigo   = txtCodigo.getText().trim();
        String nombre   = txtNombre.getText().trim();
        String credStr  = txtCreditos.getText().trim();
        String profesor = txtProfesor.getText().trim();
        boolean activo  = chkActivo.isSelected();

        int creditos;
        try {
            creditos = Integer.parseInt(credStr);
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("Los créditos deben ser un número entero.");
        }

        return new Curso(id, codigo, nombre, creditos, profesor, activo);
    }

    /**
     * Refresca la tabla con la lista de cursos recibida.
     */
    private void actualizarTabla(List<Curso> lista) {
        modeloTabla.setRowCount(0);
        for (Curso c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getCodigo(), c.getNombre(),
                c.getCreditos(), c.getProfesor(), c.isActivo()
            });
        }
    }

    /**
     * Limpia el formulario y regresa los botones a su estado inicial
     * (listo para registrar un curso nuevo).
     */
    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCreditos.setText("");
        txtProfesor.setText("");
        chkActivo.setSelected(true);
        idSeleccionado = -1;
        tabla.clearSelection();
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    /**
     * Expone la capa de negocio para que otros paneles (como MatriculaPanel,
     * que hará un compañero del grupo) puedan reutilizar la lista de cursos activos.
     */
    public CursoNegocio getNegocio() {
        return negocio;
    }
}