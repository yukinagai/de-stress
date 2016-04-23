class MainController < ApplicationController
  def index
    @heartrates = Vital.where(device: "moto360", sensor: "heartrate").where("value <> 0").order("created_at asc")
    @temperatures = Bath.where(sensor: "temperature").where("value <> 0").order("created_at asc")
  end
end
