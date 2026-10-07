package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun AuthDialog(
    language: AppLanguage,
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    var isPrototypeCredentialMode by remember { mutableStateOf(false) }
    var inputUser by remember { mutableStateOf("demo-national-admin") }
    var inputPass by remember { mutableStateOf("demo-only-password") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "भूमिका निवडा / प्रात्यक्षिक लॉगिन"
                            else if (language == AppLanguage.HINDI) "भूमिका चुनें / डेमो लॉगिन"
                            else "Select Role / Prototype Login",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = GovNavyPrimary
                        )
                        Text(
                            text = "DEMO ONLY – Replace before production",
                            fontSize = 10.sp,
                            color = EmergencyCrimson,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle tabs: Quick Role Select vs Official Mock Login
                Row(modifier = Modifier.fillMaxWidth()) {
                    FilledTonalButton(
                        onClick = { isPrototypeCredentialMode = false },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (!isPrototypeCredentialMode) GovNavyPrimary else Color(0xFFF0F0F0),
                            contentColor = if (!isPrototypeCredentialMode) Color.White else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1-Tap Roles", fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    FilledTonalButton(
                        onClick = { isPrototypeCredentialMode = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isPrototypeCredentialMode) GovNavyPrimary else Color(0xFFF0F0F0),
                            contentColor = if (isPrototypeCredentialMode) Color.White else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Admin Login", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isPrototypeCredentialMode) {
                    // Quick Role Switch List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        items(UserRole.entries) { role ->
                            val isSelected = currentRole == role
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) EmergencyCrimson else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(if (isSelected) Color(0xFFFFEBEE) else Color.White)
                                    .clickable { onRoleSelected(role) }
                                    .padding(10.dp)
                                    .testTag("role_select_${role.name}")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = role.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = GovNavyPrimary
                                        )
                                        Text(
                                            text = role.marathiTitle,
                                            fontSize = 11.sp,
                                            color = NationalGreenDark,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = role.description,
                                            fontSize = 10.sp,
                                            color = Color.Gray,
                                            lineHeight = 12.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Active",
                                            tint = EmergencyCrimson
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Prototype Admin Login Form
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = inputUser,
                            onValueChange = { inputUser = it },
                            label = { Text("Username / Username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputPass,
                            onValueChange = { inputPass = it },
                            label = { Text("Password / Password") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick fill buttons
                        Text("Prototype Preset Accounts:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            OutlinedButton(
                                onClick = {
                                    inputUser = "demo-national-admin"
                                    inputPass = "demo-only-password"
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("National", fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = {
                                    inputUser = "demo-state-admin"
                                    inputPass = "demo-only-password"
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("State (MH)", fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (inputUser.contains("national")) {
                                    onRoleSelected(UserRole.NATIONAL_ADMIN)
                                } else if (inputUser.contains("state")) {
                                    onRoleSelected(UserRole.STATE_ADMIN)
                                } else {
                                    onRoleSelected(UserRole.DISTRICT_OFFICER)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sign In (Demo Session)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
