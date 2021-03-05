package com.example.appclimadaniarroyo.fragments


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.database.DataRepository
import com.example.appclimadaniarroyo.utils.CifradoManager

class LoginFragment : Fragment() {

    lateinit var editTextUsuario: EditText
    lateinit var editTextPassword: EditText

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        editTextUsuario = v.findViewById(R.id.editTextCiudad)
        editTextPassword = v.findViewById(R.id.editTextPassword)
        val buttonLoginOk = v.findViewById<Button>(R.id.buttonLoginOk)

        sharedPreferences = this.requireActivity().getSharedPreferences("SHARED_PREF", MODE_PRIVATE)

        buttonLoginOk.setOnClickListener{
           if (procesarLogin() == 1) {
               val editor : SharedPreferences.Editor = sharedPreferences.edit();
               editor.putString("USER", editTextUsuario.text.toString())
               editor.apply()

               Toast.makeText(requireContext(), "Logeado con exito", Toast.LENGTH_LONG).show()

               findNavController().navigate(R.id.action_loginFragment_to_ciudadesMenuFragment)
           }else{

           }
        }

        return v
    }

    private fun procesarLogin() : Int {
        var estado = 0
        val dataRepository = DataRepository(requireContext())
        if (dataRepository.countUsuario() == 0){
            estado = 0
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        else{
            var passwordCifrada = CifradoManager.encryptAES(editTextPassword.text.toString())
            if (dataRepository.existeUsuario(editTextUsuario.text.toString(), passwordCifrada)){
                estado = 1
            }
            else{
                Toast.makeText(requireContext(), "Datos incorrectos", Toast.LENGTH_LONG).show()
                estado = 0
            }
        }
        return estado
    }

}