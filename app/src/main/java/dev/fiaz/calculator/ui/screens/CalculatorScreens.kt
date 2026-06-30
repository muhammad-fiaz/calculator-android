@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package dev.fiaz.calculator.ui.screens

import android.content.ClipData
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import dev.fiaz.calculator.BuildConfig
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.CalculationHistory
import dev.fiaz.calculator.domain.model.CalculatorMode
import dev.fiaz.calculator.domain.model.UnitType
import dev.fiaz.calculator.ui.components.CalculatorButton
import dev.fiaz.calculator.ui.state.CalculatorUiState
import dev.fiaz.calculator.ui.viewmodel.CalculatorViewModel
import dev.fiaz.calculator.ui.viewmodel.HistoryViewModel
import dev.fiaz.calculator.ui.viewmodel.SettingsViewModel
import dev.fiaz.calculator.ui.viewmodel.UnitConverterViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

private fun sanitizeManualInput(raw: String): String {
    return raw.filter { ch ->
        ch.isDigit() || ch in ".+-×÷^%!()πe√ "
    }
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsState by settingsViewModel.settings.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenu by remember { mutableStateOf(false) }
    val activity = LocalActivity.current

    LaunchedEffect(settingsState.appSettings.calculationCount) {
        val count = settingsState.appSettings.calculationCount
        val act = activity
        if (count > 0 && count % 15 == 0 && act != null) {
            settingsViewModel.launchReview(act) {
                // Fallback silently
            }
        }
    }

    LaunchedEffect(uiState.expression, uiState.cursorPosition) {
        if (uiState.expression.isNotEmpty()) {
            val ratio = if (uiState.expression.length > 1) {
                uiState.cursorPosition.toFloat() / uiState.expression.length.toFloat()
            } else 1f
            val target = (scrollState.maxValue * ratio).toInt()
            scrollState.animateScrollTo(target)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calculator Pro", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onOpenConverter) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Unit Converter")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val nextMode = if (uiState.mode == CalculatorMode.Normal) {
                                CalculatorMode.Scientific
                            } else {
                                CalculatorMode.Normal
                            }
                            viewModel.setMode(nextMode)
                        }
                    ) {
                        Icon(
                            imageVector = if (uiState.mode == CalculatorMode.Scientific) Icons.Default.Science else Icons.Default.Calculate,
                            contentDescription = "Switch Mode"
                        )
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("History") },
                                onClick = {
                                    showMenu = false
                                    onOpenHistory()
                                },
                                leadingIcon = { Icon(Icons.Default.History, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    onOpenSettings()
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, null) }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val wideLayout = maxWidth >= 700.dp
                if (wideLayout) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CalculatorDisplay(
                            uiState = uiState,
                            scrollState = scrollState,
                            isLargeScreen = true,
                            onExpressionEdited = viewModel::onExpressionEdited,
                            onButtonPressed = viewModel::onButtonPressed,
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.Top // Display at top in wide layout
                        )
                        CalculatorPad(
                            uiState = uiState,
                            onButton = {
                                if (uiState.isHapticEnabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }
                                viewModel.onButtonPressed(it)
                            },
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CalculatorDisplay(
                            uiState = uiState,
                            scrollState = scrollState,
                            isLargeScreen = false,
                            onExpressionEdited = viewModel::onExpressionEdited,
                            onButtonPressed = viewModel::onButtonPressed,
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), // Take up available space at the top
                            verticalAlignment = Alignment.Top
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CalculatorPad(
                            uiState = uiState,
                            onButton = {
                                if (uiState.isHapticEnabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }
                                viewModel.onButtonPressed(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.5f) // Pad at the bottom
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorDisplay(
    uiState: CalculatorUiState,
    scrollState: androidx.compose.foundation.ScrollState,
    isLargeScreen: Boolean,
    onExpressionEdited: (String, Int) -> Unit,
    onButtonPressed: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Bottom
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isResultExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    
    val expressionValue = remember(uiState.expression, uiState.cursorPosition) {
        TextFieldValue(
            text = uiState.expression,
            selection = TextRange(uiState.cursorPosition.coerceIn(0, uiState.expression.length))
        )
    }

    LaunchedEffect(uiState.expression) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val hasOperator = uiState.expression.any { it in charArrayOf('+', '-', '×', '÷', '^', '%', '(', ')', '√', '!') }
    val shouldShowResultLine = (uiState.errorMessage != null && uiState.justEvaluated) || (uiState.result.isNotBlank() && uiState.result != uiState.expression) || hasOperator
    val outputText = when {
        uiState.errorMessage != null && uiState.justEvaluated -> uiState.errorMessage
        shouldShowResultLine && uiState.result.isNotBlank() && uiState.result != uiState.expression -> uiState.result
        else -> ""
    }
    
    // Dynamic fonts: primary input/result is larger, live preview is smaller
    val baseInputSize = if (isLargeScreen) 48 else 36
    val baseOutputSize = if (isLargeScreen) 80 else 64
    
    val inputFontSize = when {
        uiState.expression.length > 25 -> (baseOutputSize * 0.4).sp
        uiState.expression.length > 15 -> (baseOutputSize * 0.6).sp
        uiState.expression.length > 8 -> (baseOutputSize * 0.8).sp
        else -> baseOutputSize.sp
    }
    
    val outputFontSize = when {
        outputText.length > 25 -> (baseInputSize * 0.4).sp
        outputText.length > 15 -> (baseInputSize * 0.6).sp
        outputText.length > 8 -> (baseInputSize * 0.8).sp
        else -> baseInputSize.sp
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = if (verticalAlignment == Alignment.Top) Arrangement.Top else Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        // Expression Area (On Top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .combinedClickable(
                    onLongClick = {
                        scope.launch {
                            clipboard.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText(
                                        "Calculation Value",
                                        uiState.expression
                                    )
                                )
                            )
                            snackbarHostState.showSnackbar("Copied to clipboard")
                        }
                    },
                    onClick = {}
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = expressionValue,
                onValueChange = { updated ->
                    val sanitized = sanitizeManualInput(updated.text)
                    val cursor = updated.selection.start.coerceIn(0, sanitized.length)
                    onExpressionEdited(sanitized, cursor)
                },
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.None,
                    showKeyboardOnFocus = false
                ),
                textStyle = MaterialTheme.typography.displayLarge.copy(
                    color = if (uiState.justEvaluated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (uiState.justEvaluated) FontWeight.Bold else FontWeight.Light,
                    fontSize = inputFontSize,
                    textAlign = TextAlign.End,
                    letterSpacing = (-1).sp
                ),
                modifier = Modifier
                    .wrapContentWidth(unbounded = true)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.hide()
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Result Area (Below Expression)
        if (outputText.isNotBlank()) {
            val resultScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .combinedClickable(
                        onClick = { isResultExpanded = !isResultExpanded },
                        onLongClick = {
                            scope.launch {
                                clipboard.setClipEntry(
                                    ClipEntry(
                                        ClipData.newPlainText(
                                            "Calculation Result",
                                            outputText
                                        )
                                    )
                                )
                                snackbarHostState.showSnackbar("Result copied")
                            }
                        }
                    ),
                horizontalAlignment = Alignment.End
            ) {
                SelectionContainer {
                    Text(
                        text = outputText,
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = when {
                                uiState.errorMessage != null -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            },
                            fontWeight = FontWeight.Medium,
                            fontSize = outputFontSize,
                            textAlign = TextAlign.End,
                            letterSpacing = (-1).sp
                        ),
                        modifier = if (isResultExpanded) Modifier.horizontalScroll(resultScrollState) else Modifier,
                        maxLines = 1,
                        overflow = if (isResultExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(baseInputSize.dp))
        }
    }
}

@Composable
private fun CalculatorPad(
    uiState: CalculatorUiState,
    onButton: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalRows = listOf(
        listOf("AC", "()", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "⌫", "=")
    )
    val scientificRows = listOf(
        listOf("sin", "cos", "tan", "√"),
        listOf("asin", "atan", "ln", "log"),
        listOf("^", "!", "π", "e"),
        listOf("AC", "()", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "⌫", "=")
    )

    val visibleRows = if (uiState.mode == CalculatorMode.Scientific) scientificRows else normalRows
    
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visibleRows.forEach { row ->
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { label ->
                    CalculatorButton(
                        label = label,
                        icon = when (label) {
                            "⌫" -> Icons.AutoMirrored.Filled.Backspace
                            "+" -> Icons.Default.Add
                            "-" -> Icons.Default.Remove
                            "×" -> Icons.Default.Close
                            "%" -> Icons.Default.Percent
                            "=" -> Icons.Default.DragHandle
                            else -> null
                        },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        backgroundColor = when (label) {
                            "=" -> MaterialTheme.colorScheme.primary
                            "+", "-", "×", "÷" -> MaterialTheme.colorScheme.primaryContainer
                            "AC" -> MaterialTheme.colorScheme.errorContainer
                            "⌫" -> MaterialTheme.colorScheme.secondaryContainer
                            "sin", "cos", "tan", "asin", "atan", "ln", "log", "√", "^", "!", "π", "e", "()", "%" -> 
                                if (uiState.mode == CalculatorMode.Scientific) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceContainerLow
                        },
                        contentColor = when (label) {
                            "=" -> MaterialTheme.colorScheme.onPrimary
                            "+", "-", "×", "÷" -> MaterialTheme.colorScheme.onPrimaryContainer
                            "AC" -> MaterialTheme.colorScheme.onErrorContainer
                            "⌫" -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        onClick = { onButton(label) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit,
    onUseExpression: (String) -> Unit
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (history.isNotEmpty()) {
                        IconButton(onClick = { 
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.clearAll() 
                        }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear all")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No calculations yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history, key = { it.id }) { item ->
                    HistoryItem(
                        item = item,
                        onClick = { 
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onUseExpression(item.expression) 
                        },
                        onDelete = { 
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.delete(item) 
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    item: CalculationHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatter = remember { DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT) }
    val dateString = remember(item.timestamp) { dateFormatter.format(Date(item.timestamp)) }

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.expression, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = "= ${item.result}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onAppearance: () -> Unit,
    onCustomization: () -> Unit,
    onAbout: () -> Unit,
    onLegal: () -> Unit,
    onLicenses: () -> Unit
) {
    val activity = LocalActivity.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        activity?.let {
            viewModel.refreshAppInfo(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { SettingsCard(title = "Appearance", subtitle = "Customize app theme and visual style", icon = Icons.Default.Palette, onClick = onAppearance) }
            item { SettingsCard(title = "Customization", subtitle = "Adjust haptics, vibration and default behavior", icon = Icons.Default.Tune, onClick = onCustomization) }
            item { 
                SettingsCard(
                    title = "Share App", 
                    subtitle = "Recommend Calculator Pro to others", 
                    icon = Icons.Default.Share, 
                    onClick = { 
                        val act = activity
                        if (act != null) {
                            viewModel.shareApp(act)
                        }
                    }
                ) 
            }
            item {
                SettingsCard(
                    title = "Write a Review",
                    subtitle = "Rate your experience on Play Store",
                    icon = Icons.Default.RateReview,
                    onClick = {
                        val act = activity
                        if (act != null) {
                            viewModel.launchReview(act) { scope.launch { snackbarHostState.showSnackbar("Review unavailable") } }
                        }
                    }
                )
            }
            item {
                SettingsCard(
                    title = "Check for Updates",
                    subtitle = "Ensure you are running the latest version",
                    icon = Icons.Default.Update,
                    onClick = {
                        val act = activity
                        if (act != null) {
                            viewModel.requestUpdateInfo(
                                onAvailable = { info ->
                                    viewModel.updateManager.startUpdateFlowForResult(info, act, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(), 999)
                                },
                                onError = { scope.launch { snackbarHostState.showSnackbar(it) } }
                            )
                        }
                    }
                )
            }
            item { SettingsCard(title = "About", subtitle = "View developer info and app version", icon = Icons.Default.Info, onClick = onAbout) }
            item { SettingsCard(title = "Open Source Licenses", subtitle = "Software libraries used in development", icon = Icons.AutoMirrored.Filled.List, onClick = onLicenses) }
            item { SettingsCard(title = "Legal", subtitle = "Privacy policy and terms of service", icon = Icons.Default.Gavel, onClick = onLegal) }
            item {
                SettingsCard(
                    title = "Report an Issue",
                    subtitle = "Found a bug? Help us fix it on GitHub",
                    icon = Icons.Default.BugReport,
                    onClick = { uriHandler.openUri("https://github.com/muhammad-fiaz/calculator-android/issues/new/choose") }
                )
            }
        }
    }
}

@Composable
fun AppearanceSettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val state by viewModel.settings.collectAsState()
    val currentTheme = state.appSettings.theme
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Appearance") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Select Theme", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeChip(selected = currentTheme == AppThemeMode.Light, label = "Light Mode", onClick = { viewModel.updateTheme(AppThemeMode.Light) })
                ThemeChip(selected = currentTheme == AppThemeMode.Dark, label = "Dark Mode", onClick = { viewModel.updateTheme(AppThemeMode.Dark) })
                ThemeChip(selected = currentTheme == AppThemeMode.System, label = "System Default", onClick = { viewModel.updateTheme(AppThemeMode.System) })
            }
        }
    }
}

@Composable
private fun ThemeChip(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) { { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) } } else null
    )
}

@Composable
fun CalculationSettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val state by viewModel.settings.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Customization") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            CustomizationToggle(title = "Haptic Feedback", description = "Tactile feedback on button presses", checked = state.appSettings.hapticsEnabled, onCheckedChange = viewModel::updateHaptics)
            CustomizationToggle(title = "Advanced Scientific Mode", description = "Launch app in scientific layout by default", checked = state.appSettings.defaultMode == CalculatorMode.Scientific, onCheckedChange = { enabled ->
                viewModel.updateMode(if (enabled) CalculatorMode.Scientific else CalculatorMode.Normal)
            })
        }
    }
}

@Composable
private fun CustomizationToggle(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun AboutScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val state by viewModel.settings.collectAsState()
    val uriHandler = LocalUriHandler.current
    val links = listOf<Triple<String, String, String>>(
        Triple("GitHub Repository", "View the source code of this project on GitHub", "https://github.com/muhammad-fiaz/calculator-android"),
        Triple("Developer's Website", "Visit the developer's personal website", BuildConfig.WEBSITE_URL),
        Triple("Organization", "Learn more about Fiaz Technologies", BuildConfig.ORG_WEBSITE_URL),
        Triple("GitHub Profile", "Explore the creator's projects and contributions", BuildConfig.DEVELOPER_GITHUB),
        Triple("Organization GitHub Profile", "View our official organization repositories", BuildConfig.ORG_GITHUB)
    )

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("About") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
        )
    }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Calculator Pro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Version ${state.versionName}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Developed by Muhammad Fiaz", style = MaterialTheme.typography.titleMedium)
                        Text("© 2025 Fiaz Technologies", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "This project is Open Source",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            items(links) { link ->
                val (title, subtitle, url) = link
                OutlinedCard(onClick = { uriHandler.openUri(url) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    SettingsRow(title = title, subtitle = subtitle)
                }
            }
        }
    }
}

@Composable
fun LegalScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val legalLinks = listOf<Triple<String, String, String>>(
        Triple("Privacy Policy", "How we handle your data", BuildConfig.PRIVACY_URL),
        Triple("Terms of Service", "Rules for using Calculator Pro", BuildConfig.TERMS_URL)
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text("Legal") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } })
    }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(legalLinks) { link ->
                val (title, subtitle, url) = link
                OutlinedCard(onClick = { uriHandler.openUri(url) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    SettingsRow(title = title, subtitle = subtitle)
                }
            }
        }
    }
}

@Composable
fun LicensesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val libraries by produceLibraries {
        runCatching {
            context.resources.openRawResource(dev.fiaz.calculator.R.raw.aboutlibraries).bufferedReader().use { it.readText() }
        }.getOrDefault("")
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Open Source Licenses") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } })
    }) { paddingValues ->
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        )
    }
}

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Converter") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    onOpenSettings()
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, null) }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Category", style = MaterialTheme.typography.titleMedium)
                PrimaryScrollableTabRow(selectedTabIndex = uiState.categories.indexOf(uiState.selectedCategory), edgePadding = 0.dp, containerColor = Color.Transparent, divider = {}) {
                    uiState.categories.forEach { category ->
                        Tab(
                            selected = uiState.selectedCategory == category,
                            onClick = {
                                if (uiState.isHapticEnabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }
                                viewModel.selectCategory(category)
                            },
                            text = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ConversionSection(
                    label = "From",
                    value = uiState.fromValue,
                    unit = uiState.fromUnit,
                    units = uiState.units,
                    onValueChange = viewModel::onFromValueChanged,
                    onUnitChange = viewModel::onFromUnitChanged,
                    isHapticEnabled = uiState.isHapticEnabled,
                    focusRequester = focusRequester
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FilledIconButton(
                        onClick = {
                            if (uiState.isHapticEnabled) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            viewModel.swapUnits()
                        }
                    ) { Icon(Icons.Default.SwapVert, contentDescription = "Swap") }
                }
                ConversionSection(
                    label = "To",
                    value = uiState.toValue,
                    unit = uiState.toUnit,
                    units = uiState.units,
                    onValueChange = {},
                    onUnitChange = viewModel::onToUnitChanged,
                    isHapticEnabled = uiState.isHapticEnabled,
                    readOnly = true
                )
            }
        }
    }
}

@Composable
private fun ConversionSection(
    label: String,
    value: String,
    unit: UnitType?,
    units: List<UnitType>,
    onValueChange: (String) -> Unit,
    onUnitChange: (UnitType) -> Unit,
    isHapticEnabled: Boolean,
    focusRequester: FocusRequester? = null,
    readOnly: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)).padding(16.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = Modifier
                    .weight(1f)
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
                textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Box {
                Surface(
                    onClick = {
                        if (isHapticEnabled) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                        expanded = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(unit?.symbol ?: "", fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    units.forEach { u ->
                        DropdownMenuItem(
                            text = { Text("${u.name} (${u.symbol})") },
                            onClick = {
                                if (isHapticEnabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }
                                onUnitChange(u)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, tonalElevation = 1.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        }
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
    }
}
