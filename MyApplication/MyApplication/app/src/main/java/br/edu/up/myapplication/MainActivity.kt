package br.edu.up.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.up.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

// Atividade principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaJornada(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TelaJornada(modifier: Modifier = Modifier) {
    // Estado para controlar o status do jogo
    var jogoAtivo by remember { mutableStateOf(true) }
    var cliquesNecessarios by remember { mutableStateOf(Random.nextInt(1, 51)) }
    var contadorCliques by remember { mutableStateOf(0) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarDialogoConquista by remember { mutableStateOf(false) }
    var mostrarMensagemDesistencia by remember { mutableStateOf(false) }

    // Estado para controlar se a tela de conquista está sendo mostrada
    var mostrandoTelaConquista by remember { mutableStateOf(false) }

    // Array de imagens
    val imagens = listOf(
        R.drawable.medieval_town__8__by_darkmage73_dg67hqr_350t,   // Imagem Inicial
        R.drawable.e36317e86bd94bc19ff7fbd85bba0384,   // Imagem Mediana
        R.drawable.untitled_design,     // Imagem Final
        R.drawable.ghjhuntitled_design, // Imagem de Conquista
        R.drawable.untitled_design // Imagem de Desistência
    )

    // Determinar a imagem atual com base no número de cliques
    val imagemAtual = when {
        contadorCliques >= cliquesNecessarios -> imagens[3]
        contadorCliques >= cliquesNecessarios * 0.66 -> imagens[2]
        contadorCliques >= cliquesNecessarios * 0.33 -> imagens[1]
        else -> imagens[0]
    }

    // Determinar o texto atual com base no progresso do jogo
    val textoAtual = when {
        contadorCliques >= cliquesNecessarios -> "Parabéns! Você chegou ao fim da jornada. Sua determinação o levou ao triunfo. Desfrute da sensação de conquista e da paisagem que você alcançou."
        mostrarMensagemDesistencia -> "Você decidiu encerrar a jornada por aqui. Às vezes, a melhor escolha é saber a hora de parar. Mas lembre-se, você sempre pode tentar novamente."
        contadorCliques >= cliquesNecessarios * 0.66 -> "Agora você está em um lugar acolhedor e cheio de vida. O calor ao seu redor é reconfortante, e o fim da jornada está à vista. Continue, a vitória está próxima!"
        contadorCliques >= cliquesNecessarios * 0.33 -> "Você sente o ambiente ao seu redor aquecer. A jornada está se tornando mais confortável, mas ainda há um longo caminho a percorrer. Mantenha o foco e siga em frente!"
        else -> "Você está no início de sua jornada, em um lugar frio e sombrio. A escuridão ao seu redor desafia sua coragem. Continue avançando e veja se consegue encontrar a luz."
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Exibir o texto na parte superior da tela
        Text(
            text = textoAtual,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir a imagem no centro da tela
        val imagemPainter: Painter = painterResource(id = imagemAtual)
        Image(
            painter = imagemPainter,
            contentDescription = "Imagem da Jornada",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        if (jogoAtivo) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        if (contadorCliques < cliquesNecessarios) {
                            contadorCliques++
                        }
                        if (contadorCliques >= cliquesNecessarios) {
                            jogoAtivo = false
                            mostrandoTelaConquista = true
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Clique aqui: $contadorCliques")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        jogoAtivo = false
                        mostrarDialogo = true
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Desistir")
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Novo jogo?") },
            text = { Text("Deseja iniciar um novo jogo?") },
            confirmButton = {
                Button(
                    onClick = {
                        jogoAtivo = true
                        cliquesNecessarios = Random.nextInt(1, 51)
                        contadorCliques = 0
                        mostrarDialogo = false
                    }
                ) {
                    Text("Sim")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        mostrarMensagemDesistencia = true
                        mostrarDialogo = false
                    }
                ) {
                    Text("Não")
                }
            }
        )
    }

    // Exibir a tela de conquista por alguns segundos antes de mostrar o diálogo de reinício
    if (mostrandoTelaConquista) {
        LaunchedEffect(Unit) {
            delay(5000) // Espera 5 segundos
            mostrarDialogoConquista = true
            mostrandoTelaConquista = false
        }
    }

    if (mostrarDialogoConquista) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConquista = false },
            title = { Text("Parabéns!") },
            text = { Text("Você alcançou a meta!") },
            confirmButton = {
                Button(
                    onClick = {
                        jogoAtivo = true
                        cliquesNecessarios = Random.nextInt(1, 51)
                        contadorCliques = 0
                        mostrarDialogoConquista = false
                    }
                ) {
                    Text("Reiniciar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelaJornadaPreview() {
    MyApplicationTheme {
        TelaJornada()
    }
}
