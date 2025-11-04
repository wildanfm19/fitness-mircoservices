import { Box, Button, AppBar, Toolbar, Typography, Container, Avatar } from "@mui/material"
import { useContext, useEffect, useState } from "react"
import { AuthContext } from "react-oauth2-code-pkce"
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom"
import { setCredentials } from "./store/authSlice"
import { useDispatch } from "react-redux"
import ActivityForm from "./components/ActivityForm"
import ActivityList from "./components/ActivityList"
import ActivityDetail from "./components/ActivityDetail"

const ActivitiesPage = () => {
  // track the id of a newly added activity so the list can show a loading state
  const [newlyAddedId, setNewlyAddedId] = useState(null);

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 6 }}>
      <Box component="section" sx={{ mb: 3 }}>
        <ActivityForm onActivityAdded={(id) => setNewlyAddedId(id)} />
      </Box>
      <ActivityList newActivityId={newlyAddedId} onNewActivityReady={() => setNewlyAddedId(null)} />
    </Container>
  )
}


function App() {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext)
  const dispatch = useDispatch();

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
    }
  }, [token, tokenData, dispatch])

  return (
    <Router>
      <AppBar position="static" color="default" elevation={1} sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Avatar sx={{ bgcolor: 'primary.main' }}>F</Avatar>
            <Typography variant="h6" component="div">Fitness Studio</Typography>
          </Box>
          <Box>
            {!token ? (
              <Button variant="contained" color="primary" onClick={() => logIn()}>Login</Button>
            ) : (
              <Button variant="outlined" color="primary" onClick={() => logOut()}>Logout</Button>
            )}
          </Box>
        </Toolbar>
      </AppBar>

      <Box component="main" sx={{ minHeight: 'calc(100vh - 64px)', py: 4 }}>
        {!token ? (
          <Container maxWidth="sm" sx={{ mt: 8, textAlign: 'center' }}>
            <Typography variant="h5" gutterBottom>Welcome</Typography>
            <Typography color="text.secondary">Please login to view and track your activities.</Typography>
          </Container>
        ) : (
          <Routes>
            <Route path="/activities" element={<ActivitiesPage />} />
            <Route path="/activities/:id" element={<ActivityDetail />} />
            <Route path="/" element={token ? <Navigate to="/activities" replace /> : <div>Welcome Please Login</div>} />
          </Routes>
        )}
      </Box>
    </Router>
  )
}

export default App
