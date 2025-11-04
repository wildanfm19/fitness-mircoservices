import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField, Card, CardContent } from "@mui/material";
import React, { useState } from "react";
import { addActivity } from "../services/api";

const ActivityForm = ({onActivityAdded}) => {
  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {},
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await addActivity(activity);
      // if parent provided a callback, give them the created activity id so UI can track loading
      if (onActivityAdded) onActivityAdded(response?.data?.id);
      setActivity({ type: "RUNNING", duration: "", caloriesBurned: "" });
    } catch (error) {
      console.error("Error adding activity:", error);
    }
  };

  return (
    <Card sx={{ mb: 3 }}>
      <CardContent>
        <Box component="form" onSubmit={handleSubmit} sx={{ mb: 0 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel>Activity type</InputLabel>
        <Select
          value={activity.type}
          onChange={(e) => setActivity({ ...activity, type: e.target.value })}
        >
          <MenuItem value="RUNNING">Running</MenuItem>
          <MenuItem value="CYCLING">Cycling</MenuItem>
          <MenuItem value="WALKING">Walking</MenuItem>
          <MenuItem value="SWIMMING">Swimming</MenuItem>
        </Select>
      </FormControl>
      <TextField
        fullWidth
        label="Duration (minutes)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.duration}
        onChange={(e) => setActivity({ ...activity, duration: e.target.value })}
      />
      <TextField
        fullWidth
        label="Calories Burned"
        type="number"
        sx={{ mb: 2 }}
        value={activity.caloriesBurned}
        onChange={(e) =>
          setActivity({ ...activity, caloriesBurned: e.target.value })
        }
      />
      {/* Additional metrics based on activity type can be added here */}
          <Button type="submit" variant="contained" color="primary" sx={{ textTransform: 'none' }}>
            Add Activity
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ActivityForm;
