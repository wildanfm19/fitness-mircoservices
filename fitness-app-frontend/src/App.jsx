import { Box, Button } from "@mui/material"
import { useContext, useState } from "react"
import { AuthContext } from "react-oauth2-code-pkce"
import { BrowserRouter as Router , Navigate , Route , Routes , useLocation } from "react-router"
import { setCredentials } from "./store/authSlice"
import { useDispatch } from "react-redux"
import { useEffect } from "react"
import ActivityForm from "./components/ActivityForm"
import ActivityList from "./components/ActivityList"
import ActivityDetail from "./components/ActivityDetail"

const ActivitiesPage = () => {
 return  (<Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
    <ActivityForm onActivityAdded = {() => window.location.reload()}/>
    <ActivityList />
  </Box>)
}


function App() {
  const { token , tokenData , logIn , logOut , isAuthenticated } = useContext(AuthContext)
  const dispatch = useDispatch();
  const [authReady , setAuthReady] = useState(false);


  useEffect(() => {
    if(token ){
      dispatch(setCredentials({token , user : tokenData}));
      setAuthReady(true);
    }
  } ,[token , tokenData , dispatch])

  return (
    <Router>
    { !token ? (
      <Button variant="contained" color="#dc004e"
            onClick={() => {
              logIn();
            }}>Login</Button>
            ) : (
                <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
                <Routes>
                  <Route path="/activities" element= {<ActivitiesPage />}/>
                  <Route path="/activities/:id" element= {<ActivityDetail />}/>

                   <Route path="/" element= {token ? <Navigate to= "/activities" replace /> : <div>Welcome Please Login</div>}/>
                </Routes>
              </Box>
            )}
    </Router>
  )
}

export default App
