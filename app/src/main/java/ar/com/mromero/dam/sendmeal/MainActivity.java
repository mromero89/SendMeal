package ar.com.mromero.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textocarga;
    SeekBar seekbar;
    Switch switchCargaInicial;
    Button botonRegistrar;
    CheckBox checkboxAceptar;
    EditText etContrasena, etRepetirContrasena, etMail, etNroTarjeta, etCVV, etMesVenc, etAnnoVenc;
    RadioButton rbDeb, rbCred;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textocarga = (TextView) findViewById(R.id.textViewCargaInicial);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        switchCargaInicial = (Switch) findViewById(R.id.switchCargaInicial);
        botonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        checkboxAceptar = (CheckBox) findViewById(R.id.checkBoxAceptarTerms);
        etContrasena = (EditText) findViewById(R.id.editTextPassword);
        etRepetirContrasena = (EditText) findViewById(R.id.editTextRepetirPassword);
        etMail = (EditText) findViewById(R.id.editTextEmail);
        etNroTarjeta = (EditText) findViewById(R.id.editTextNroTarjeta);
        rbCred = (RadioButton) findViewById(R.id.radioButtonCredito);
        rbDeb = (RadioButton) findViewById(R.id.radioButtonDebito);
        //etTipoTarjeta = (EditText)
        etCVV = (EditText) findViewById(R.id.editTextCVV);
        etMesVenc = (EditText) findViewById(R.id.editTextMesVencimiento);
        etAnnoVenc = (EditText) findViewById(R.id.editTextAnnoVencimiento);

        botonRegistrar.setEnabled(false);


        switchCargaInicial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //setea que si el Switch de carga inicial esta activado, se muestra la Seekbar para seleccionar monto
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    textocarga.setVisibility(View.VISIBLE);
                    seekbar.setVisibility(View.VISIBLE);
                }
                else
                {
                    textocarga.setVisibility(View.GONE);
                    seekbar.setVisibility(View.GONE);
                }

            }
        });


        seekbar.setOnSeekBarChangeListener(
                //con el cambio de la Seekbar se muestra el importe seleccinoado en el TextView textocarga
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        textocarga.setText("Carga inicial: $"+i);

                        int a = seekBar.getProgress();


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        checkboxAceptar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                botonRegistrar.setEnabled(isChecked);



            }
        });

        botonRegistrar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validar();

                    }
                }
        );



    }

    private boolean validar(){
        //metodo que chequea que todos los campos obligatorios esten completos y la password sea
        //igual a la password confirmada
        boolean valido = true;


        if (etContrasena.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "La contraseña no puede ser vacia", Toast.LENGTH_SHORT);
            toast.show();

        }
        if (!(etContrasena.getText().toString().equals(etRepetirContrasena.getText().toString()))) {
            valido = false;
            Toast toast = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (etMail.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "Debe completar el e-mail", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!(rbDeb.isChecked() || rbCred.isChecked())) {
            valido = false;
            Toast toast = Toast.makeText(this, "Por favor, elija un tipo de tarjeta", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (etNroTarjeta.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "Complete el número de la tarjeta", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (etCVV.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "Falta el CVV", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (etMesVenc.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "Falta el mes de vencimiento de la tarjeta", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (etAnnoVenc.getText().toString().equals("")) {
            valido = false;
            Toast toast = Toast.makeText(this, "Falta el año de vencimiento de la tarjeta", Toast.LENGTH_SHORT);
            toast.show();
        }


        if (!validarmail()){
            valido = false;
            Toast toast = Toast.makeText(this, "Por favor, complete correctamente el e-mail", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (switchCargaInicial.isChecked()){
            if (seekbar.getProgress() == 0){
                valido = false;
                Toast toast = Toast.makeText(this, "Carga Inicial debe ser mayor a $0", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (valido) {
            valido = validarfecha();
        }
        else
            validarfecha();

        if (valido){
            Toast toast = Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT);
            toast.show();
        }


        return valido;
    }

    private boolean validarmail(){
        if (etMail.getText().toString().contains("@")){
            String [] aux = etMail.getText().toString().split("@");
            if (aux.length > 1){
                int tam = aux[1].length();
                if (tam < 3) {
                    return false;

                }
            }
            else
                return false;


        }
        else
            return false;

        return true;
    }

    private boolean validarfecha() {
        Date d = Calendar.getInstance().getTime();
        Calendar hoy = Calendar.getInstance();
        hoy.add(Calendar.MONTH, 3);
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat formatocomun = new SimpleDateFormat("dd/MM/yyyy");

        //verificar que los campos no esten vacios!
        if (etMesVenc.getText().toString().equals("") || etAnnoVenc.getText().toString().equals("")){

            return false;
        }
        else{
            int mesvenc = Integer.parseInt(etMesVenc.getText().toString());
            int annovenc = Integer.parseInt(etAnnoVenc.getText().toString());

            if (mesvenc < 1 || mesvenc > 12){
                Toast toast = Toast.makeText(this, "Ingrese un mes válido", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            if (annovenc < 1900 || annovenc > 2999){
                Toast toast = Toast.makeText(this, "Ingrese un año válido", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            Calendar calendartarjeta = Calendar.getInstance();
            calendartarjeta.set(annovenc, mesvenc-1, 30);
            Date fechatarjeta = calendartarjeta.getTime();


            Date d2 = hoy.getTime();
            String prueba = formatocomun.format(d2);
            String stringmes = mes.format(d);
            String stringvencimiento = formatocomun.format(fechatarjeta);
            //System.out.println(formato.format(d));
            int resultado = d2.compareTo(fechatarjeta);
            if (resultado > 0){
                Toast toast = Toast.makeText(this, "La tarjeta está vencida o tiene un vencimiento muy próximo", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }




        return true;
    }

}