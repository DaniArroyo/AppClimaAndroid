package com.example.appclimadaniarroyo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.database.DataRepository
import com.example.appclimadaniarroyo.database.Usuario
import com.example.appclimadaniarroyo.utils.CifradoManager
import com.scottyab.aescrypt.AESCrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.GeneralSecurityException

lateinit var editTextUsuario: EditText
lateinit var editTextPassword: EditText
lateinit var editTextNombre: EditText

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_register, container, false)

        editTextUsuario = v.findViewById(R.id.editTextRegisterUsuario)
        editTextPassword = v.findViewById(R.id.editTextRegisterPassword)
        editTextNombre = v.findViewById(R.id.editTextRegisterNombre)
        val buttonRegisterOk = v.findViewById<Button>(R.id.buttonRegisterOk)

        buttonRegisterOk.setOnClickListener {
            procesarRegister()
        }

        return v
    }

    private fun procesarRegister() {
        val dataRepository = DataRepository(requireContext())
        if (editTextPassword.text.toString() == "" || editTextUsuario.text.toString() == "" || editTextNombre.text.toString() == "") {
            Toast.makeText(requireContext(), "Rellende todos los campos", Toast.LENGTH_LONG).show()
        } else {
            var passwordCifrada = CifradoManager.encryptAES(editTextPassword.text.toString())
            var nombreCifrado = CifradoManager.encryptAES(editTextNombre.text.toString())

            if (dataRepository.insert(
                    Usuario(
                        editTextUsuario.text.toString(),
                        passwordCifrada,
                        nombreCifrado
                    )
                ) == -1
            ) {
                cleanEditTexts()
                Toast.makeText(requireContext(), "Usuario repetido", Toast.LENGTH_LONG).show()
            } else {
                cleanEditTexts()
                Toast.makeText(requireContext(), "Registrado con Exito", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun cleanEditTexts() {
        editTextUsuario.getText().clear();
        editTextPassword.getText().clear();
        editTextNombre.getText().clear();
    }

}