import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router';
import { getActivities, getActivityDetail } from '../services/api';
import { Card, CardContent, Grid, Typography, CardActionArea, CardHeader, CircularProgress, Box } from '@mui/material';

const ActivityList = ({ newActivityId, onNewActivityReady }) => {
  const [activities , setActivities] = useState([]);
  const navigate = useNavigate();

  const fetchActivities = async() => {
    try{
      const response = await getActivities();
      setActivities(response.data);
    }catch(error){
      console.error("Error fetching activities:", error);
    }
  }; 

  useEffect(() => {
    fetchActivities();
  } , []);

  // When a new activity is added, refetch list so the new item appears
  useEffect(() => {
    if (!newActivityId) return;
    fetchActivities();
  }, [newActivityId]);

  // Poll for recommendation/detail readiness for the newly added activity
  useEffect(() => {
    if (!newActivityId) return;

    let cancelled = false;
    const interval = setInterval(async () => {
      try {
        const resp = await getActivityDetail(newActivityId);
        // consider the detail ready when we get a successful response with data
        if (resp && resp.data) {
          clearInterval(interval);
          if (!cancelled) {
            // let parent know it's ready so it can stop showing loading state
            onNewActivityReady && onNewActivityReady(newActivityId);
            // refresh list to pick up any changes
            fetchActivities();
          }
        }
      } catch {
        // ignore errors while polling (resource may not exist yet)
      }
    }, 1500);

    return () => {
      cancelled = true;
      clearInterval(interval);
    };
  }, [newActivityId, onNewActivityReady]);
  return (
   <Grid container spacing={2}>
      {activities.map((activity) => {
        const isPending = newActivityId && activity.id === newActivityId;
        return (
        <Grid item xs={12} sm={6} md={4} key={activity.id}>
          <Card elevation={2} sx={{ position: 'relative', transition: 'transform .12s ease, box-shadow .12s ease' }}>
            <CardActionArea onClick={() => !isPending && navigate(`/activities/${activity.id}`)} sx={{ pointerEvents: isPending ? 'none' : 'auto' }}>
              <CardHeader title={activity.type} subheader={activity.date ? new Date(activity.date).toLocaleDateString() : ''} />
              <CardContent>
                <Typography sx={{ mb: 1 }}>Duration: <strong>{activity.duration}</strong> min</Typography>
                <Typography>Calories Burned: <strong>{activity.caloriesBurned}</strong></Typography>
              </CardContent>
            </CardActionArea>

            {isPending && (
              <Box sx={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: 'rgba(255,255,255,0.7)' }}>
                <CircularProgress />
              </Box>
            )}
          </Card>
        </Grid>
      )})}
   </Grid>
  )
}

export default ActivityList