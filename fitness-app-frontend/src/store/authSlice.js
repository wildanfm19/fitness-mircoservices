import { createSlice } from '@reduxjs/toolkit'



const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    userId : localStorage.getItem('userId') || null,
  },
  reducers: {
    setCredentials: (state, action) =>  {
      
    },
    logOut: (state) => {
      
    },
  },
});

export const { setCredentials, logOut } = authSlice.actions
export default authSlice.reducer